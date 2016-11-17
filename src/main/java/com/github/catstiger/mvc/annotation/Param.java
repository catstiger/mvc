package com.github.catstiger.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.catstiger.mvc.converter.ValueConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
  /**
   * 用于对应Http参数名称
   */
  String value() default "";
  
  /**
   * 对于被标注的参数，使用哪个转换器将之从String转换为某个Object，如果系统提供的缺省
   * 的转换器不能满足要求的情况下使用这个转换器。
   */
  public Class<? extends ValueConverter<?>> converter() default ValueConverter.None.class;
}
