package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.StringUtils;

public class BooleanValueConverter extends PrimitiveConverter<Boolean> {

  @Override
  public Boolean convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed)) {
      return null;
    }
    return new Boolean(trimmed);
  }

}
