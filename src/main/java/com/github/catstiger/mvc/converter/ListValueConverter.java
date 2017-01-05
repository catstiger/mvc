package com.github.catstiger.mvc.converter;

import java.util.List;

import com.github.catstiger.utils.CollectionUtils;

public class ListValueConverter implements ValueConverter<List<?>>{
  private ArrayValueConverter arrayValueConverter;
  private Class<?> elementType;
  
  public ListValueConverter(Class<?> elementType) {
    this.elementType = elementType;
    this.arrayValueConverter = new ArrayValueConverter(this.elementType);
  }

  @Override
  public List<?> convert(Object value) {
    Object results = arrayValueConverter.convert(value);
    
    return CollectionUtils.arrayToList(results);
  }

}
