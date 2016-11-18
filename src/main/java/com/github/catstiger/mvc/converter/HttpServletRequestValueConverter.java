package com.github.catstiger.mvc.converter;

import javax.servlet.http.HttpServletRequest;

import com.github.catstiger.mvc.RequestObjectHolder;

public class HttpServletRequestValueConverter implements ValueConverter<HttpServletRequest> {

  @Override
  public HttpServletRequest convert(Object value) {
    return RequestObjectHolder.getRequest();
  }

}
