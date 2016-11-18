package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.config.Initializer;

import strman.Strman;

public abstract class RequestParser {
  private static Logger logger = LoggerFactory.getLogger(RequestParser.class);
  
  public static final String DATA_TYPE_JSON = ".json";
  public static final String DATA_TYPE_TEXT = ".txt";
  public static final String DATA_TYPE_HTM = ".htm";
  public static final String DATA_TYPE_HTML = ".html";
  
  /**
   * 剔除URI中的前缀，后缀，最终得到可以lookup action的URI片段
   * @param request HttpServletRequest
   * @return 
   */
  public static String getRequestUri(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return "";
    }
    
    Initializer cfg = Initializer.getInstance();
    
    uri = Strman.removeLeft(uri, request.getContextPath());
    uri = Strman.removeLeft(uri, cfg.getUriPrefix());
    int dotIndex = uri.indexOf(".");
    if(dotIndex > 0) {
      uri = uri.substring(0, dotIndex);
    }
    
    if(uri.endsWith("/")) {
      uri = Strman.removeRight(uri, "/");
    }
    
    return uri;
  }
  
  /**
   * 获取请求的数据类型，例如uri为/do/some/thing.json，请求的类型就是JSON数据
   * @param request
   * @return
   */
  protected static String getRequiredDataType(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return "";
    }
    
    int dotIndex = uri.indexOf(".");
    if(dotIndex > 0) {
      return uri.substring(dotIndex, uri.length());
    }
    
    return "";
  }
  
  /**
   * 判断是否为JSON请求，URI后缀为.htm或者.html的为非JSON请求，其他都是JSON请求
   * @return 如果为JSON请求，返回true
   */
  public static boolean isJsonRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return false;
    }
    
    return (Strman.endsWith(uri, DATA_TYPE_JSON, false) 
        || (!Strman.endsWith(uri, DATA_TYPE_TEXT, false) && !Strman.endsWith(uri, DATA_TYPE_HTM, false)  && !Strman.endsWith(uri, DATA_TYPE_HTM, false)));
  }
  
}