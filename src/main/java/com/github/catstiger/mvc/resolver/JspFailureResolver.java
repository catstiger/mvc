package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

public class JspFailureResolver extends AbstractResponseResolver {
  
  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    if(value != null && value instanceof Throwable) {
      Throwable ex = ((Throwable) value);
      throw new RuntimeException(ex.getMessage(), ex);
    }  
  }

}
