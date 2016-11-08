package com.github.catstiger.mvc.converter;

public class StringValueConverter implements ValueConverter<String> {

  @Override
  public String stringToObject(String strValue) {
    return strValue;
  }

}
