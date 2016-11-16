package com.github.catstiger.mvc.converter;

import com.github.catstiger.mvc.util.StringUtils;

public class IntegerValueConverter extends PrimitiveConverter<Integer> {

  @Override
  public Integer convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    return (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
  }

}

