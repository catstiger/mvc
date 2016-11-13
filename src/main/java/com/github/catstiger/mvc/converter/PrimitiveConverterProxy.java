package com.github.catstiger.mvc.converter;

public class PrimitiveConverterProxy implements ValueConverter<Object> {
  private ValueConverter<?> orginConverter;
  private Class<?> type;
  public PrimitiveConverterProxy(ValueConverter<?> orginConverter, Class<?> type) {
    this.orginConverter = orginConverter;
    this.type = type;
  }

  @Override
  public Object convert(Object value) {
    Object returns = orginConverter.convert(value);
    if(returns != null) {
      return returns;
    } else {
      if(type == int.class) {
        return 0;
      } else if (type == long.class) {
        return 0L;
      } else if (type == byte.class) {
        return 0;
      } else if (type == short.class) {
        return 0.0f;
      } else  if (type == double.class) {
        return 0.0D;
      } else if (type == boolean.class) {
        return false;
      } else {
        return 0;
      }
      
    }
  }

}
