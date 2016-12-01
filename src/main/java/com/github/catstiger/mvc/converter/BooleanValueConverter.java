package com.github.catstiger.mvc.converter;

import com.github.catstiger.mvc.util.StringUtils;

public class BooleanValueConverter extends PrimitiveConverter<Boolean> {

  @Override
  public Boolean convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return new Boolean(trimmed);
  }

}
