package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

public class TextFailureResolver extends AbstractFailureResonseResolver {

  @Override
  protected void handleReadableException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, String string) {
    renderText(response, string);
    
  }

  @Override
  protected void handleUnexpectException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Throwable ex) {
    renderText(response, ex.getMessage());
  }

}
