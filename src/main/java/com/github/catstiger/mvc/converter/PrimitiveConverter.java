package com.github.catstiger.mvc.converter;

public abstract class PrimitiveConverter<T> implements ValueConverter<T> {
  protected static boolean isHexNumber(String value) {
    int index = (value.startsWith("-") ? 1 : 0);
    return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
  }
}
