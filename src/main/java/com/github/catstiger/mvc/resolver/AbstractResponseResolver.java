package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.service.RequestParser;
import com.github.catstiger.utils.WebUtils;

public abstract class AbstractResponseResolver implements ResponseResolver {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static final String ATTR_NAME_COLLECTION = "list";
  protected static final String ATTR_NAME_PRIMITIVE = "data";
  
  /**
   * 渲染JSON请求。
   * @param json
   */
  protected void renderJson(HttpServletResponse response, String json) {
    WebUtils.render(response, json, "application/json");
  }
  
  /**
   * 渲染JSON数据，并且当使用GET方法请求JSON数据的时候，默认提供一天（86400秒）的缓存周期。
   * @param json JSON数据
   */
  protected void renderJsonWithCache(HttpServletRequest request, HttpServletResponse response, String json) {
    if (request == null) {
      throw new RuntimeException("HttpServletRequest is null.");
    }
    if (response == null) {
      throw new RuntimeException("HttpServletResponse is null.");
    }
    if (!"GET".equalsIgnoreCase(request.getMethod())) {
      WebUtils.setNoCacheHeader(response);
    } else {
      WebUtils.setExpiresHeader(response, Initializer.getInstance().getCacheSeconds());
    }
    
    renderJson(response, json);
  }
  
  /**
   * 直接输出普通文本.
   */
  protected void renderText(HttpServletResponse response, String text) {
    WebUtils.render(response, text, "text/plain;charset=UTF-8");
  }
  
  /**
   * 渲染Text数据，并且当使用GET方法请求JSON数据的时候，默认提供一天（86400秒）的缓存周期。
   * @param text Text数据
   */
  protected void renderTextWithCache(HttpServletRequest request, HttpServletResponse response, String text) {
    if (request == null) {
      throw new RuntimeException("HttpServletRequest is null.");
    }
    if (response == null) {
      throw new RuntimeException("HttpServletResponse is null.");
    }
    if (!"GET".equalsIgnoreCase(request.getMethod())) {
      WebUtils.setNoCacheHeader(response);
    } else {
      WebUtils.setExpiresHeader(response, Initializer.getInstance().getCacheSeconds());
    }
    
    renderText(response, text);
  }
  
  /**
   * 判断是否是JSON请求
   */
  protected boolean isJsonRequest(HttpServletRequest request) {
    if (request == null) {
      throw new RuntimeException("HttpServletRequest is null.");
    }
    return RequestParser.isJsonRequest(request);
  }
  
  /**
   * 返回请求的数据类型
   */
  protected String getRequiredDataType(HttpServletRequest request) {
    if (request == null) {
      throw new RuntimeException("HttpServletRequest is null.");
    }
    return RequestParser.getRequiredDataType(request);
  }
}
