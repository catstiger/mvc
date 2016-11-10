package com.github.catstiger.mvc.resovler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.config.Initializer;

import strman.Strman;

public abstract class RequestParser {
  private static Logger logger = LoggerFactory.getLogger(RequestParser.class);
  /**
   * 剔除URI中的前缀，后缀，最终得到可以lookup action的URI片段
   * @param request HttpServletRequest
   * @return 
   */
  public static String getServiceURI(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      logger.warn("URI is null !");
      return "";
    }
    
    Initializer cfg = Initializer.getInstance();
    
    uri = Strman.removeLeft(uri, request.getContextPath());
    uri = Strman.removeLeft(uri, cfg.getUriPrefix());
    uri = Strman.removeRight(uri, cfg.getUriSuffix());
    
    if(uri.endsWith("/")) {
      uri = Strman.removeRight(uri, "/");
    }
    
    return uri;
  }
  
}
