package com.github.catstiger.mvc.resolver;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.config.Initializer;

import strman.Strman;

public abstract class RequestParser {
  private static Logger logger = LoggerFactory.getLogger(RequestParser.class);
  
  public static final Set<String> STATIC_URI = new HashSet<String>(20);
  static {
    STATIC_URI.add(".doc");
    STATIC_URI.add(".xls");
    STATIC_URI.add(".xlsx");
    STATIC_URI.add(".docx");
    STATIC_URI.add(".ppt");
    STATIC_URI.add(".pptx");
    STATIC_URI.add(".jpg");
    STATIC_URI.add(".png");
    STATIC_URI.add(".bmp");
    STATIC_URI.add(".js");
    STATIC_URI.add(".css");
    STATIC_URI.add(".html");
    STATIC_URI.add(".jpeg");
    STATIC_URI.add(".swf");
  }
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
  
  public static boolean isStatic(String uri) {
    if(uri == null) {
      throw new RuntimeException("URI must not be null");
    }
    int dotIndex = uri.indexOf(".");
    if(dotIndex < 0) {
      return false;
    }
    String ext = uri.substring(dotIndex);
    return STATIC_URI.contains(ext);
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
   * 判断是否为JSON请求，URI后缀为.json或者.没有后缀的的为JSON请求
   * @return 如果为JSON请求，返回true
   */
  public static boolean isJsonRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return false;
    }
    
    return (Strman.endsWith(uri, ".json") || uri.indexOf(".") < 0);
  }
  
  /**
   * 根据请求，判断是否需要重定向（forward）到一个JSP，后缀为.do,.action,.htm,.jsp为JSP请求。
   */
  public static boolean isJspRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return false;
    }
    return Strman.endsWith(uri, ".do") || Strman.endsWith(uri, ".action") || Strman.endsWith(uri, ".htm") || Strman.endsWith(uri, ".action");
  }
  
  /**
   * 根据请求，判断是否需要渲染一个TEXT（通常用于AJAX方式获取一个HTML片段），后缀为.txt,.text的为TEXT请求
   */
  public static boolean isTextRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return false;
    }
    return Strman.endsWith(uri, ".txt") || Strman.endsWith(uri, ".text");
  }
  
}
