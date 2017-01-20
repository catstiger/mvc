package com.github.catstiger.mvc.resolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

public class JspFailureResolver extends AbstractFailureResonseResolver {
  
  @Override
  protected void handleReadableException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, String string) {
    try {
      request.setAttribute("errorMessage", string);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, string);
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }

  @Override
  protected void handleUnexpectException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Throwable ex) {
    ex.printStackTrace();
    try {
      request.setAttribute("javax.servlet.error.exception", ex);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }

}
