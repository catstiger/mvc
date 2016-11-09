package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.StringUtils;

public class DoubleValueConverter extends PrimitiveConverter<Double> {

  @Override
  public Double convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    return new Double(trimmed);
  }

}
