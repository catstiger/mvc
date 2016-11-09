package com.github.catstiger.mvc.converter;

import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.ServletObjectHolder;

public class HttpServletResponseValueConverter implements ValueConverter<HttpServletResponse> {

  @Override
  public HttpServletResponse convert(Object value) {
    return ServletObjectHolder.getResponse();
  }

}
