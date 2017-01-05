package com.github.catstiger.mvc.converter;

import com.github.catstiger.utils.StringUtils;

public class LongValueConverter extends PrimitiveConverter<Long> {

  @Override
  public Long convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed) || !StringUtils.isNumber(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
  }
}
