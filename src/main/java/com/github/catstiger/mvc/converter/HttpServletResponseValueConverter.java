package com.github.catstiger.mvc.converter;

import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.RequestObjectHolder;

public class HttpServletResponseValueConverter implements ValueConverter<HttpServletResponse> {

  @Override
  public HttpServletResponse convert(Object value) {
    return RequestObjectHolder.getResponse();
  }

}
