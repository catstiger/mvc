package com.github.catstiger.mvc.converter;

import java.lang.reflect.Array;

import com.github.catstiger.mvc.util.ClassUtils;


/**
 * 数组转换
 * @author catstiger
 *
 */
public class ArrayValueConverter implements ValueConverter<Object> {
  private Class<?> elementType;
  
  public ArrayValueConverter(Class<?> elementType) {
    this.elementType = elementType;
  }

  @Override
  public Object convert(Object value) {
    if(value == null) {
      return null;
    }
    Object[] array;
    if(value.getClass().isArray()) {
      array = (Object[]) value;
    } else {
      array = new Object[]{value};
    }
    
    if(array == null || array.length == 0) {
      return null;
    }
    
    Class<?> componentType = elementType;
    if(elementType.isPrimitive()) { //原始数据类型使用包装类来获取转换器
      componentType = ClassUtils.primitiveToWrapper(elementType);
    }
    ValueConverter<?> valueConverter = ConverterFactory.getConverter(componentType);
    Object results = Array.newInstance(elementType, array.length);
    
    for(int i = 0; i < array.length; i++) {
      Object obj = valueConverter.convert(array[i]);
      Array.set(results, i, obj);
    }
    return results;
  }
}
