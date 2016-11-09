package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.util.Assert;

public class ConverterFactory {

  private static final Map<Class<?>, ValueConverter<?>> SINGLE_TYPE_CONVERTERS;
  static {
    Map<Class<?>, ValueConverter<?>> singleTypeConverter = new HashMap<Class<?>, ValueConverter<?>>();
    singleTypeConverter.put(Long.class, new LongValueConverter());
    singleTypeConverter.put(Integer.class, new IntegerValueConverter());
    singleTypeConverter.put(Short.class, new ShortValueConverter());
    singleTypeConverter.put(Byte.class, new ByteValueConverter());
    singleTypeConverter.put(BigInteger.class, new BigIntegerValueConverter());
    singleTypeConverter.put(Float.class, new FloatValueConvert());
    singleTypeConverter.put(Double.class, new DoubleValueConverter());
    singleTypeConverter.put(String.class, new StringValueConverter());
    singleTypeConverter.put(Date.class, new DateValueConverter());
    singleTypeConverter.put(BigDecimal.class, new BigDecimalValueConverter());
    singleTypeConverter.put(Boolean.class, new BooleanValueConverter());

    SINGLE_TYPE_CONVERTERS = Collections.unmodifiableMap(singleTypeConverter);
  }
  
  public static boolean isBean(Class<?> targetClass) {
    boolean isBean = false;
    // 原始数据类型
    if (targetClass.isPrimitive()) {
    } else if (SINGLE_TYPE_CONVERTERS.containsKey(targetClass)) {
    } else if (ClassUtils.isAssignable(targetClass, Collection.class)) {
    } else if (ClassUtils.isAssignable(targetClass, Map.class)) {
    } else if (isPrimitiveArray(targetClass)) {
    } else if (targetClass.isArray()) {
    } 
    else {
      isBean = true;
    }
    return isBean;
  }
  
  /**
   * 根据给定的<code>Class</code>找到合适的{@link ValueConverter}的实现
   * 
   * @param targetClass
   * @return
   */
  public static ValueConverter<?> getConverter(Class<?> targetClass) {
    if (targetClass == null) {
      throw new RuntimeException("目标类型不能为null.");
    }

    ValueConverter<?> converter = SINGLE_TYPE_CONVERTERS.get(String.class);
    
    // 原始数据类型
    if (targetClass.isPrimitive()) {
      converter = SINGLE_TYPE_CONVERTERS.get(ClassUtils.primitiveToWrapper(targetClass));
    } 
    // 原始数据类型Wrapper,Date, String, BigDecimal etc.
    else if (SINGLE_TYPE_CONVERTERS.containsKey(targetClass)) {
      converter = SINGLE_TYPE_CONVERTERS.get(targetClass);
    } 
    // Collection及其子类
    else if (ClassUtils.isAssignable(targetClass, Collection.class)) {
      
    } 
    // Map及其子类
    else if (ClassUtils.isAssignable(targetClass, Map.class)) {
      
    } 
    //由原始数据类型组成的数组
    else if (isPrimitiveArray(targetClass)) {
      converter = new ArrayValueConverter(ClassUtils.primitiveToWrapper(targetClass.getComponentType()));
    }
    //数组
    else if (targetClass.isArray()) {
      converter = new ArrayValueConverter(targetClass.getComponentType());
    } 
    // Java Bean
    else {
      converter = new BeanValueConverter(targetClass);
    }

    return converter;
  }
  
  private static Boolean isPrimitiveArray(Class<?> clazz) {
    Assert.notNull(clazz, "Class must not be null");
    return (clazz.isArray() && clazz.getComponentType().isPrimitive());
  }

  

  public static void main(String[] args) {
    List<String> strs = new ArrayList<String>();
    strs.add("a");
    
    try {
      for(int i = 0; i < strs.getClass().getGenericInterfaces().length; i++) {
        System.out.println(ClassUtils.isAssignable(strs.getClass(), Collection.class));
      }
    }  catch (SecurityException e) {
      e.printStackTrace();
    }
    
  }
}
