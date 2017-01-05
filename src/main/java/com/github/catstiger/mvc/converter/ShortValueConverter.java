package com.github.catstiger.mvc.converter;

import com.github.catstiger.utils.StringUtils;

public class ShortValueConverter extends PrimitiveConverter<Short> {

  @Override
  public Short convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
  }

}
