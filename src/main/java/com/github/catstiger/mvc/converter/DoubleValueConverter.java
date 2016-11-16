package com.github.catstiger.mvc.converter;

import com.github.catstiger.mvc.util.StringUtils;

public class DoubleValueConverter extends PrimitiveConverter<Double> {

  @Override
  public Double convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed)) {
      return null;
    }
    return new Double(trimmed);
  }

}
