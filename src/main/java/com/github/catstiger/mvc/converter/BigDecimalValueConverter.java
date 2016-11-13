package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class BigDecimalValueConverter extends PrimitiveConverter<BigDecimal> {

  @Override
  public BigDecimal convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed)) {
      return null;
    }
    return new BigDecimal(trimmed);
  }

}
