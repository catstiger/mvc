package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class BigDecimalValueConverter implements ValueConverter<BigDecimal> {

  @Override
  public BigDecimal convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    return new BigDecimal(trimmed);
  }

}
