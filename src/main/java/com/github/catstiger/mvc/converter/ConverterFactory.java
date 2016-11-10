package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
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
  

  private static final Map<Class<?>, ValueConverter<?>> SINGLE_TYPE_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  static {
    SINGLE_TYPE_CONVERTERS.put(Long.class, new LongValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Integer.class, new IntegerValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Short.class, new ShortValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Byte.class, new ByteValueConverter());
    SINGLE_TYPE_CONVERTERS.put(BigInteger.class, new BigIntegerValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Float.class, new FloatValueConvert());
    SINGLE_TYPE_CONVERTERS.put(Double.class, new DoubleValueConverter());
    SINGLE_TYPE_CONVERTERS.put(String.class, new StringValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Date.class, new DateValueConverter());
    SINGLE_TYPE_CONVERTERS.put(java.sql.Date.class, new DateValueConverter());
    SINGLE_TYPE_CONVERTERS.put(BigDecimal.class, new BigDecimalValueConverter());
    SINGLE_TYPE_CONVERTERS.put(Boolean.class, new BooleanValueConverter());
  }
  
  private static final Map<Class<?>, ValueConverter<?>> ARRAY_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  private static final Map<Class<?>, ValueConverter<?>> LIST_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  private static final Map<Class<?>, ValueConverter<?>> SET_CONVERTERS = new HashMap<Class<?>, ValueConverter<?>>(100);
  
  
  public static ValueConverter<?> getConverter(Class<?> targetClass) {
    return getConverter(targetClass, null);
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
      converter = SINGLE_TYPE_CONVERTERS.get(ClassUtils.primitiveToWrapper(targetClass));
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
