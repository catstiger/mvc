package com.github.catstiger.mvc.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.catstiger.mvc.util.CollectionUtils;

public class SetValueConverter implements ValueConverter<Set<?>>{
  private ArrayValueConverter arrayValueConverter;
  private Class<?> elementType;
  
  public SetValueConverter(Class<?> elementType) {
    this.elementType = elementType;
    this.arrayValueConverter = new ArrayValueConverter(this.elementType);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Set<?> convert(Object value) {
    Object results = arrayValueConverter.convert(value);
    
    List list = CollectionUtils.arrayToList(results);
    Set set = new HashSet(list.size());
    set.addAll(list);
    
    return set;
  }

}
