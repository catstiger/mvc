package com.github.catstiger.mvc.converter;

public class StringValueConverter implements ValueConverter<String> {

  @Override
  public String convert(Object value) {
    if(value == null) {
      return null;
    }
    return value.toString();
  }

}
