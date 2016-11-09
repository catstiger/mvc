package com.github.catstiger.mvc.converter;

/**
 * 用于数组，List，Set等对象的转换
 * @author catstiger
 *
 * @param <T> List , Set ...
 */
public abstract class MultiObjectValueConverter<T> implements ValueConverter<T> {
  protected Class<?> elementType = String.class;

  MultiObjectValueConverter(Class<?> elementType) {
    if(elementType != null) {
      this.elementType = elementType;
    }
  }
  
  public Class<?> getElementType() {
    return elementType;
  }

  public void setElementType(Class<?> elementType) {
    this.elementType = elementType;
  }
}
