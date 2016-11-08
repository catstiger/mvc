package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.math.NumberUtils;

public class DoubleValueConverter implements ValueConverter<Double> {

  @Override
  public Double stringToObject(String strValue) {
    if(NumberUtils.isNumber(strValue)) {
      return Double.parseDouble(strValue);
    }
    return null;
  }

}
