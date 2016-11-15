package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConverterFactory {
  private static Logger logger = LoggerFactory.getLogger(ConverterFactory.class);
  
  private static final Map<Class<?>, ValueConverter<?>> simpleConverters = new HashMap<Class<?>, ValueConverter<?>>(100);
  public static final Map<Class<?>, ValueConverter<?>> SIMPLE_CONVERTERS;
  static {
    simpleConverters.put(Long.class, new LongValueConverter());
    simpleConverters.put(Integer.class, new IntegerValueConverter());
    simpleConverters.put(Short.class, new ShortValueConverter());
    simpleConverters.put(Byte.class, new ByteValueConverter());
    simpleConverters.put(BigInteger.class, new BigIntegerValueConverter());
    simpleConverters.put(Float.class, new FloatValueConvert());
    simpleConverters.put(Double.class, new DoubleValueConverter());
    simpleConverters.put(String.class, new StringValueConverter());
    simpleConverters.put(Date.class, new DateValueConverter());
    simpleConverters.put(java.sql.Date.class, new DateValueConverter());
    simpleConverters.put(BigDecimal.class, new BigDecimalValueConverter());
    simpleConverters.put(Boolean.class, new BooleanValueConverter());
    
    SIMPLE_CONVERTERS = Collections.unmodifiableMap(simpleConverters);
  }
  
  private static final Map<Class<?>, ValueConverter<?>> SINGLE_TYPE_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  static {
    SINGLE_TYPE_CONVERTERS.putAll(simpleConverters);
  }
  
  private static final Map<Class<?>, ValueConverter<?>> ARRAY_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  private static final Map<Class<?>, ValueConverter<?>> LIST_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  private static final Map<Class<?>, ValueConverter<?>> SET_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  
  
  public static ValueConverter<?> getConverter(Class<?> targetClass) {
    return getConverter(targetClass, null);
  }
  
  public static boolean isPojo(Class<?> clazz) {
    return (!clazz.isPrimitive() && !clazz.isArray() && !ConverterFactory.SIMPLE_CONVERTERS.containsKey(clazz) && clazz  != List.class && clazz != Set.class && clazz != Map.class);
  }
  /**
   * 根据给定的<code>Class</code>找到合适的{@link ValueConverter}的实现
   * 
   * @param targetClass
   * @return
   */
  public static ValueConverter<?> getConverter(Class<?> targetClass, Class<?> elementClass) {
    if (targetClass == null) {
      throw new RuntimeException("目标类型不能为null.");
    }
    
    ValueConverter<?> converter;
    
    if(ARRAY_CONVERTERS.containsKey(targetClass)) {
      converter = ARRAY_CONVERTERS.get(targetClass);
    } else if (LIST_CONVERTERS.containsKey(elementClass)) {
      converter = LIST_CONVERTERS.get(elementClass);
    } else if (SET_CONVERTERS.containsKey(elementClass)) {
      converter = SET_CONVERTERS.get(elementClass);
    } else if (SINGLE_TYPE_CONVERTERS.containsKey(targetClass)) {
      converter = SINGLE_TYPE_CONVERTERS.get(targetClass);
    } else {
      converter = newConverter(targetClass, elementClass);
      if(converter != null) {
        if(targetClass.isArray()) {
          ARRAY_CONVERTERS.put(targetClass, converter);
        } else if (List.class == targetClass) {
          LIST_CONVERTERS.put(elementClass, converter);
        } else if (Set.class == targetClass) {
          SET_CONVERTERS.put(elementClass, converter);
        } else {
          SINGLE_TYPE_CONVERTERS.put(targetClass, converter);
        }
      }
    }
    
    if(converter == null) {
      logger.warn("{}没有合适的转换器，使用StringValueConverter", targetClass.getName());
      converter = new StringValueConverter();
    }
    
    //logger.debug("Class {} - Converter - {}", targetClass, converter.getClass());
    return converter;
  }
  
  
  private static ValueConverter<?> newConverter(Class<?> targetClass, Class<?> elementClass) {
    ValueConverter<?> converter;
    //数组
    if (targetClass.isArray()) {
      converter = new ArrayValueConverter(targetClass.getComponentType());
    } 
    // 原始数据类型
    else if (targetClass.isPrimitive()) {
      ValueConverter<?> vc = SINGLE_TYPE_CONVERTERS.get(ClassUtils.primitiveToWrapper(targetClass));
      converter = new PrimitiveConverterProxy(vc, targetClass);
    } 
    //List
    else if (List.class == targetClass && elementClass != null) {
      converter = new ListValueConverter(elementClass);
    } 
    //Set
    else if (Set.class == targetClass && elementClass != null) {
      converter = new SetValueConverter(elementClass);
    }
    // 原始数据类型Wrapper,Date, String, BigDecimal etc.
    else if (SINGLE_TYPE_CONVERTERS.containsKey(targetClass)) {
      converter = SINGLE_TYPE_CONVERTERS.get(targetClass);
    } 
    // Java Bean
    else {
      converter = new BeanValueConverter(targetClass);
    }
    
    return converter;
  }
}
