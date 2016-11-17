package com.github.catstiger.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.catstiger.mvc.resolver.ResponseResolver;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface API {
  /**
   * 
   * <strong>用于方法</strong>
   * <ul>
   *     <li>如果为空字符串或者<code>null</code>，对应的URL片段为方法名全小写，且单词之间用"_"分割。
   *     <li>如果不为空，那么value就是对应的URI的后半部分，例如，URI为/do/some/thing,那么，value为/thing
   * </ul>
   * 
   * @return
   */
  public String value() default "";
  
  /**
   * 定义API执行结果的处理器
   * @return
   */
  public Class<? extends ResponseResolver> resolver() default ResponseResolver.None.class;
  
}
