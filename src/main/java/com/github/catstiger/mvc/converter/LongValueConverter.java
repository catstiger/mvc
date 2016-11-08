package com.github.catstiger.mvc.converter;

import org.apache.commons.lang3.math.NumberUtils;

public class LongValueConverter implements ValueConverter<Long> {

  @Override
  public Long stringToObject(String strValue) {
    if(NumberUtils.isNumber(strValue)) {
      return Long.parseLong(strValue);
    }
    
    return null;
  }

}
