package com.github.catstiger.mvc.converter;

import com.github.catstiger.utils.StringUtils;

public class IntegerValueConverter extends PrimitiveConverter<Integer> {

  @Override
  public Integer convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
  }

}

