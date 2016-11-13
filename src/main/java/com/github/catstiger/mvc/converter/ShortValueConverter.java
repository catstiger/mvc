package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.StringUtils;

public class ShortValueConverter extends PrimitiveConverter<Short> {

  @Override
  public Short convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
  }

}
