package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

public class TextFailureResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    if(value != null) {
      if(value instanceof Throwable) {
        renderText(response, ((Throwable) value).getMessage());
      } else {
        renderText(response, (String) value);
      }
    } 

  }

}
