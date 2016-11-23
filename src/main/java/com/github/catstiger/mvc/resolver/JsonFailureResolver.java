package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.config.ApiResource;

public class JsonFailureResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    JsonModel jsonModel = new JsonModel(JsonModel.ERROR_UNKNOWN, (String) value);
    String json = JSON.toJSONString(jsonModel);
    renderJson(response, json);
  }

}
