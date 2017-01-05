package com.github.catstiger.mvc.converter;

import com.github.catstiger.utils.StringUtils;

public class FloatValueConvert extends PrimitiveConverter<Float> {

  @Override
  public Float convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return new Float(trimmed);
  }

}
