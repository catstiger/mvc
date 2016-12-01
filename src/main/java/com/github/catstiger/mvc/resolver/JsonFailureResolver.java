package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.config.ApiResource;

public class JsonFailureResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    String msg;
    if(value != null && value instanceof Throwable) {
      msg = ((Throwable) value).getMessage();
    } else {
      msg = (String) value;
    }
    JsonModel jsonModel = new JsonModel(JsonModel.ERROR_UNKNOWN, msg);
    String json = JSON.toJSONString(jsonModel);
    renderJson(response, json);
  }

}
