package com.github.catstiger.mvc.converter;

import java.math.BigDecimal;

import com.github.catstiger.mvc.util.StringUtils;

public class BigDecimalValueConverter extends PrimitiveConverter<BigDecimal> {

  @Override
  public BigDecimal convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return new BigDecimal(trimmed);
  }

}
