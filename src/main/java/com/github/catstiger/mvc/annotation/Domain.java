package com.github.catstiger.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Domain {
  /**
   * <strong>用于类</strong>
   * <ul>
   *     <li>如果为空字符串或者<code>null</code>，对应的URL为类名（或者spring bean id）全小写，且单词之间用"_"分割。
   *     <li>如果不为空，那么value就是对应的URI的前半部分，例如，URI为/do/some/thing,那么，value为/do/some
   * </ul>
   * @return
   */
  String value() default "";
  /**
   * 用于Service不是Spring管理的情况，true表示单例模式，系统只初始化一次实例；否则，每次访问该service，都创建实例。
   */
  boolean singleton() default true;
}
