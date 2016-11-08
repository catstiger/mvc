package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.math.NumberUtils;

public class FloatValueConvert implements ValueConverter<Float> {

  @Override
  public Float stringToObject(String strValue) {
    if(NumberUtils.isNumber(strValue)) {
      return Float.parseFloat(strValue);
    }
    return null;
  }

}
