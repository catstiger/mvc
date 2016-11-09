package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * @author catstiger
 *
 */
public class ArrayValueConverter extends MultiObjectValueConverter<Object[]> {
  
  /**
   * 构造一个新的ArrayValueConverter
   * @param typeOfElement 目标数组中每一个元素的类型
   */
  public ArrayValueConverter(Class<?> elementType) {
    super(elementType);
  }

  @Override
  public Object[] convert(Object value) {
    if(value == null) {
      return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    Object[] array;
    if(value.getClass().isArray()) {
      array = (Object[]) value;
    } else {
      array = new Object[]{value};
    }
    
    if(array == null || array.length == 0) {
      return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    
    Object[] results = new Object[array.length];
    ValueConverter<?> valueConverter = ConverterFactory.getConverter(getElementType());
    
    for(int i = 0; i < array.length; i++) {
      results[i] = valueConverter.convert(array[i]);
    }
    
    return results;
  }
}
