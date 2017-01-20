package com.github.catstiger.mvc.resolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.config.ApiResource;

public class JsonFailureResolver extends AbstractFailureResonseResolver {
  
  @Override
  protected void handleReadableException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, String string) {
    JsonModel jsonModel = new JsonModel(JsonModel.ERROR_UNKNOWN, string);
    String json = JSON.toJSONString(jsonModel);
    logger.error(string);
    renderJson(response, json);
  }

  @Override
  protected void handleUnexpectException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Throwable ex) {
    logger.error(ex.getMessage());
    ex.printStackTrace();
    try {
      request.setAttribute("javax.servlet.error.exception", ex);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
