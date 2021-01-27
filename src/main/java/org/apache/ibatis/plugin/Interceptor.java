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

import java.util.Properties;

/**
 * Mybatis插件的顶级接口
 *    自定义插件步骤：
 *        需要实现这个接口
 *        然后交给{@link org.apache.ibatis.session.Configuration#interceptorChain}
 *
 * @author Clinton Begin
 */
public interface Interceptor {
  /**
   * 真正方法被拦截执行的逻辑
   *
   * @param invocation 主要目的是将多个参数进行封装
   */
  Object intercept(Invocation invocation) throws Throwable;

  default Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  /**
   * 通过该方法可以给拦截器设置一些属性，主要是用于可扩展性
   */
  default void setProperties(Properties properties) {
    // NOP
  }

}
