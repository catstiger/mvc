package com.github.catstiger.mvc.converter;

import javax.servlet.http.HttpServletRequest;

import com.github.catstiger.mvc.ServletObjectHolder;

public class HttpServletRequestValueConverter implements ValueConverter<HttpServletRequest> {

  @Override
  public HttpServletRequest convert(Object value) {
    return ServletObjectHolder.getRequest();
  }

}
