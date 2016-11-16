package com.github.catstiger.mvc.converter;

import com.github.catstiger.mvc.util.StringUtils;

public class LongValueConverter extends PrimitiveConverter<Long> {

  @Override
  public Long convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
  }

}
