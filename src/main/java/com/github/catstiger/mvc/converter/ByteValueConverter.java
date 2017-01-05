package com.github.catstiger.mvc.converter;

import com.github.catstiger.utils.StringUtils;

public class ByteValueConverter extends PrimitiveConverter<Byte> {

  @Override
  public Byte convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if(isNull(trimmed) || "".equals(trimmed)) {
      return null;
    }
    return (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
  }

}

