package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.StringUtils;

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

