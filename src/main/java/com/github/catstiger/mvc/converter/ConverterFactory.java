package com.github.catstiger.mvc.converter;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.util.ClassUtils;
import com.github.catstiger.mvc.util.ReflectUtils;

public abstract class ConverterFactory {
  private static Logger logger = LoggerFactory.getLogger(ConverterFactory.class);
  
  private static final Map<Class<?>, ValueConverter<?>> simpleConverters = new HashMap<Class<?>, ValueConverter<?>>(160);
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
  
  private static final Map<Class<?>, ValueConverter<?>> SINGLE_TYPE_CONVERTERS = new ConcurrentHashMap<Class<?>, ValueConverter<?>>(160);
  static {
    SINGLE_TYPE_CONVERTERS.putAll(simpleConverters);
  }
  
  private static final Map<Class<?>, ValueConverter<?>> CUSTOMER_CONVERTERS = new ConcurrentHashMap<Class<?>, ValueConverter<?>>(160);
  
  private static final Map<String, ValueConverter<?>> COLLECTION_CONVERTERS = new ConcurrentHashMap<String, ValueConverter<?>>(160);
  
  public static boolean isPojo(Class<?> clazz) {
    return (!clazz.isPrimitive() && !clazz.isArray() && !ConverterFactory.SIMPLE_CONVERTERS.containsKey(clazz) && clazz  != List.class && clazz != Set.class && clazz != Map.class);
  }
  
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
    if(elementClass != null) {
      String key = targetClass.getName() + "@" + elementClass.getName();
      if(COLLECTION_CONVERTERS.containsKey(key)) {
        converter = COLLECTION_CONVERTERS.get(key);
      } else {
        converter = newConverter(targetClass, elementClass);
        COLLECTION_CONVERTERS.put(key, converter);
      }
    } else {
      if (SINGLE_TYPE_CONVERTERS.containsKey(targetClass)) {
        converter = SINGLE_TYPE_CONVERTERS.get(targetClass);
      } else {
        converter = newConverter(targetClass, elementClass);
        SINGLE_TYPE_CONVERTERS.put(targetClass, converter);
      }
    }
    
    if(converter == null) {
      logger.warn("{}没有合适的转换器，使用StringValueConverter", targetClass.getName());
      converter = new StringValueConverter();
    }
    
    return converter;
  }
  
  public static ValueConverter<?> getConverter(Parameter parameter) {
    if(parameter == null) {
      throw new RuntimeException("Parameter must not be null.");
    }
    //如果参数标注了转换器
    Param paramAnnotation = parameter.getAnnotation(Param.class);
    if(paramAnnotation != null) {
      Class<?> converterClass = paramAnnotation.converter();
      if(converterClass != null) {
        if(converterClass != ValueConverter.None.class) {
          if(CUSTOMER_CONVERTERS.containsKey(converterClass)) {
            return CUSTOMER_CONVERTERS.get(converterClass);
          } else {
            ValueConverter<?> converter = (ValueConverter<?>) ReflectUtils.instantiate(converterClass);
            CUSTOMER_CONVERTERS.put(converterClass, converter);
            return converter;
          }
        }
      }
    }
    //如果没有标注转换器，则自动根据参数类型构造转换器
    Class<?> paramType = parameter.getType();
    Class<?> elementType = ReflectUtils.getParameterActualType(parameter);

    ValueConverter<?> converter = ConverterFactory.getConverter(paramType, elementType);
    if(converter == null) {
      throw new RuntimeException("No ValueConverter found.");
    }
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
    //Request
    else if (ClassUtils.isAssignable(targetClass, HttpServletRequest.class, false)) {
      converter = new HttpServletRequestValueConverter();
    }
    //Response
    else if (ClassUtils.isAssignable(targetClass, HttpServletResponse.class, false)) {
      converter = new HttpServletResponseValueConverter();
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
