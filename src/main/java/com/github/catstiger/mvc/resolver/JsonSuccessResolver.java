package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.config.ApiResource;

/**
 * 用于向Response中写入处理结果的JSON数据，处理结果将被封装成为一个{@link JsonModel}对象，用于表达本次操作是否成功。
 * @author catstiger
 *
 */
public class JsonSuccessResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    if(value != null) {
      String json = JSON.toJSONString(value);
      renderJson(response, json);
    }
  }

}
