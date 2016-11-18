package com.github.catstiger.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;


public class ServletObjectHolderMock {
  
  public static void init() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    RequestObjectHolder.setRequest(request);
    RequestObjectHolder.setResponse(response);
  }
  
  public static void setRequestParameters(Map<String, Object> params) {
    RequestObjectHolder.setRequestParameters(params);
  }
}
