/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Clinton Begin
 */
public class Plugin implements InvocationHandler {

  private final Object target;
  private final Interceptor interceptor;
  private final Map<Class<?>, Set<Method>> signatureMap;

  private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
    this.target = target;
    this.interceptor = interceptor;
    this.signatureMap = signatureMap;
  }

  /**
   * 如果该对象{@code target}实现了被拦截的接口，生成目标对象的代理对象返回
   * 1. 解析Interceptor拦截的接口及方法
   * 2. 获取{@code target}对象实现了被拦截的所有接口
   *    2.1 如果有  生成一个代理对象返回（使用JDK动态代理的方式）
   *    2.2 没有    返回原始对象{@code target}
   *
   * @param target      目标对象
   * @param interceptor 拦截器
   */
  public static Object wrap(Object target, Interceptor interceptor) {
    // 1.解析该拦截器所拦截的所有接口及对应拦截接口的方法
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    Class<?> type = target.getClass();
    // 2.获取目标对象实现的所有被拦截的接口
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    // 3.目标对象有实现被拦截的接口，生成代理对象并返回
    if (interfaces.length > 0) {
      return Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new Plugin(target, interceptor, signatureMap));
    }
    // 目标对象没有实现被拦截的接口，直接返回原对象
    return target;
  }

  /**
   * 生成的代理类的真正调用逻辑
   * 1. 判断是否当前方法是被拦截方法
   *    1.1 是被拦截方法   走拦截器的的逻辑
   *    1.2 不是拦截方法   调用目标对象的方法
   *
   * @param proxy    被代理对象
   * @param method   当前调用方法
   * @param args     方法的真实入参
   * @return         方法的执行结果
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
      if (methods != null && methods.contains(method)) {
        return interceptor.intercept(new Invocation(target, method, args));
      }
      return method.invoke(target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }

  /**
   * 解析Interceptor实现类上的注解
   * 将该实现类上所有Signature注解属性解析成Map<Class<?>, Set<Method>>对象返回
   *
   * 所以实现拦截器的时候，要在实现类上使用Intercepts和Signature注解
   * 表示该拦截器拦截哪个类哪个方法（以及方法参数）
   *
   * @param interceptor Interceptor接口实现类
   * @return map对象
   */
  private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
    Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
    // issue #251
    if (interceptsAnnotation == null) {
      throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
    }
    Signature[] sigs = interceptsAnnotation.value();
    Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
    for (Signature sig : sigs) {
      Set<Method> methods = signatureMap.computeIfAbsent(sig.type(), k -> new HashSet<>());
      try {
        Method method = sig.type().getMethod(sig.method(), sig.args());
        methods.add(method);
      } catch (NoSuchMethodException e) {
        throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
      }
    }
    return signatureMap;
  }

  /**
   * 收集拦截的接口并返回
   * 1. 获取该类实现的所有接口
   * 2. 遍历所有实现的接口
   * 3. 如果该接口存在map对象中
   * 4. 则添加到集合中
   * 5. 最后集合转数组并返回
   *
   * @param type 被拦截类
   * @param signatureMap <拦截的接口--拦截该接口的具体方法>映射
   * @return   拦截的接口
   */
  private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
    Set<Class<?>> interfaces = new HashSet<>();
    while (type != null) {
      for (Class<?> c : type.getInterfaces()) {
        if (signatureMap.containsKey(c)) {
          interfaces.add(c);
        }
      }
      type = type.getSuperclass();
    }
    return interfaces.toArray(new Class<?>[0]);
  }

}
