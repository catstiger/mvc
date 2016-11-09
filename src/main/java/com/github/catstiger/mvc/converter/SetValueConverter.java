package com.github.catstiger.mvc.converter;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

public class SetValueConverter extends MultiObjectValueConverter<Set<?>> {
  private ArrayValueConverter arrayValueConverter;

  public SetValueConverter(Class<?> elementType) {
    super(elementType);
    arrayValueConverter = new ArrayValueConverter(elementType);
  }
  
  @Override
  public Set<?> convert(Object value) {
    Object[] objects = arrayValueConverter.convert(value);
    if(objects == null || objects.length == 0) {
      return Collections.emptySet();
    }
    
    return Sets.newHashSet(objects);
  }

}
