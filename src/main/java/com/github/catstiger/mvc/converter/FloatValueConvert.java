package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.StringUtils;

public class FloatValueConvert extends PrimitiveConverter<Float> {

  @Override
  public Float convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    return new Float(trimmed);
  }

}
