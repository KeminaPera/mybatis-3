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
package com.keminapera.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Method;
import java.util.Properties;

@Intercepts({
  @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MyInterceptor implements Interceptor {
  private String desc;
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object target = invocation.getTarget();
    System.out.println("被拦截的类" + target.getClass().getName());
    Method method = invocation.getMethod();
    System.out.println("被拦截的方法：" + method);
    Object[] args = invocation.getArgs();
    System.out.println("被拦截方法的参数" + args);
    System.out.println("设置的成员属性" + desc);
    return method.invoke(target, args);
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    desc = properties.getProperty("desc");
  }
}
