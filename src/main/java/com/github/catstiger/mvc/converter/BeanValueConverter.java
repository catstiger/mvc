package com.github.catstiger.mvc.converter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.util.ClassUtils;
import com.github.catstiger.mvc.util.ReflectUtils;

public class BeanValueConverter implements ValueConverter<Object> {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private Class<?> targetType;
  
  public BeanValueConverter(Class<?> targetType) {
    logger.debug("Converter for {}", targetType);
    this.targetType = targetType;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object convert(Object value) {
    if(targetType == null) {
      logger.warn("Unknow target object type.");
      return null;
    }
    
    if(value == null) {
      return null;
    }
    
    if(!ClassUtils.isAssignable(value.getClass(), Map.class, false)) {
      return null;
    }
    
    Object result = null;
    
    try {
      result = targetType.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
    Map<String, Object> valueMap = (Map<String, Object>) value;
    PropertyDescriptor[] propertyDescs = ReflectUtils.getPropertyDescriptors(targetType);
    
    for(PropertyDescriptor propDesc : propertyDescs) {
      if("class".equals(propDesc.getName())) {
        continue;
      }  
      //对应该属性的转换器
      ValueConverter<?> converter = null;
      Method write = propDesc.getWriteMethod();
      
      if(ClassUtils.isAssignable(propDesc.getPropertyType(), Collection.class)) {
        //如果参数为Collection的实现类，获得其泛型参数类型
        Parameter param = write.getParameters()[0];
        Class<?> elementType = ReflectUtils.getActualTypeOfCollectionElement(param);
        if(elementType == null) {
          elementType = String.class;
        }
        converter = ConverterFactory.getConverter(propDesc.getPropertyType(), elementType);
      } else {
        converter = ConverterFactory.getConverter(propDesc.getPropertyType());
      }
      
      if(converter == null) {
        logger.debug("没有对应的转换器 {}#{}", targetType.getSimpleName(), propDesc.getName());
        continue;
      }
      //对应该属性的原始数据
      Object val = null;
      if(valueMap.containsKey(propDesc.getName())) {
        val = valueMap.get(propDesc.getName());
      } else if (valueMap.containsKey(propDesc.getName().toLowerCase())) {
        val = valueMap.get(propDesc.getName().toLowerCase());
      }
      //原始数据转换为该属性需要的数据 
      Object converted = converter.convert(val);
      //写入对象
      if(write != null) {
        try {
          write.invoke(result, converted);
        } catch (Exception e) {
          e.printStackTrace();
          logger.error(e.getMessage() + " property is " + propDesc.getName() + " writer method " + write.getName() 
            + "\n value is " + converted + "\n converter is " + converter.getClass());
          throw new RuntimeException(e.getMessage());
        } 
      }
    }
    
    return result;
  }

  public Class<?> getTargetType() {
    return targetType;
  }

  public void setTargetType(Class<?> targetType) {
    this.targetType = targetType;
  }
}
