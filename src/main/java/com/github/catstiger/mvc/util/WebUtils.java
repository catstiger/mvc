package com.github.catstiger.mvc.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.common.net.HttpHeaders;

public final class WebUtils {
  private WebUtils() {
    
  }
  
  /**
   * 设置客户端缓存过期时间 的Header.
   */
  public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
    // Http 1.0 header, set a fix expires date.
    response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
    // Http 1.1 header, set a time after now.
    response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
  }

  /**
   * 设置禁止客户端缓存的Header.
   */
  public static void setNoCacheHeader(HttpServletResponse response) {
    // Http 1.0 header
    response.setDateHeader(HttpHeaders.EXPIRES, 1L);
    response.addHeader(HttpHeaders.PRAGMA, "no-cache");
    // Http 1.1 header
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
  }
  
  /**
   * 向HttpServletResponse中渲染文本
   * @param response HttpServletResponse
   * @param text 要渲染的文本
   * @param contentType content type.
   */
  public static void render(HttpServletResponse response, String text, String contentType) {
    if (response == null) {
      throw new RuntimeException("HttpServletResponse must not be null.");
    }
    try {
      response.setContentType(contentType);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(text);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
