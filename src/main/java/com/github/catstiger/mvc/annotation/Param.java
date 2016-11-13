package com.github.catstiger.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface Param {
  /**
   * 用于对应Http参数名称
   */
  String value() default "";
  /**
   * 用于标明参数类型，如果参数为数组，Collection，则标明其元素类型
   */
  Class<?> elementType() default Object.class;
}
