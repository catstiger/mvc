package com.github.catstiger.mvc.converter;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class ListValueConverter extends MultiObjectValueConverter<List<?>> {
  private ArrayValueConverter arrayValueConverter;

  public ListValueConverter(Class<?> elementType) {
    super(elementType);
    arrayValueConverter = new ArrayValueConverter(elementType);
  }
  
  @Override
  public List<?> convert(Object value) {
    Object[] objects = arrayValueConverter.convert(value);
    if(objects == null || objects.length == 0) {
      return Collections.emptyList();
    }
    
    return Lists.newArrayList(objects);
  }

}
