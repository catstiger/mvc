package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

/**
 * 用于向Response直接渲染一段文本，通常用于通过AJAX获取HTML片段
 *
 */
public class DefaultTextSuccessResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    if(value != null) {
      renderText(response, (String) value);
    } 
  }

}
