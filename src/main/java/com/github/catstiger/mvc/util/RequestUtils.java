package com.github.catstiger.mvc.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.google.common.net.HttpHeaders;

public final class RequestUtils {
  /**
   * 根据Request信息判断是否是请求一个json对象。Header的x-requested-with属性如果
   * 为XMLHttpRequest，则直接返回<code>true</code>。
   * Header的Accept属性中如果包含application/x-json或text/x-json,则表示请求json对象。如果Request
   * 的参数Accept为x-json，也表示请求一个json对象。客户端可以这样设置：
   * <pre>
   * XMLHttpRequest xhr = ...;
   * xhr.setHeader("Accept", "text/x-json;charset=UTF-8");
   * </pre>
   * 如果使用extjs<br>
   * <pre>
   * Ext.Ajax.defaultHeaders = {
    'Accept': 'application/x-json;text/x-json;charset=UTF-8'
    };
   * </pre>
   * 如果使用jquery:<br>
   * <pre>
   * $.ajax({ url:'user/index.do',
   *      data: {'model.name':'sam'},
   *      async: true,
   *      <B>beforeSend: function(xhr) {xhr.setRequestHeader('Accept': 'application/x-json;text/x-json;charset=UTF-8');}</B>
   * });
   * </pre> 
   * 如果不使用Ajax 方式:<br>
   * <pre>
   * your-url.jsp?Accept=x-json&..
   * </pre>
   * @return
   */
  public static boolean isJsonRequest(HttpServletRequest request) {   
   
    //首先验证x-requested-with参数
    String reqWith = request.getHeader("x-requested-with");
    if(reqWith != null && reqWith.toLowerCase().endsWith( "XMLHttpRequest".toLowerCase())) {
      return true;
    }
    //AngularJS
    reqWith = request.getHeader("access-control-request-headers");
    if(reqWith != null && reqWith.indexOf("x-requested-with") >= 0) {
      return true;
    }
    
    //Apicloud 的$api请求
    reqWith = request.getHeader("user-agent");
    if(reqWith != null && reqWith.toLowerCase().indexOf("apicloud") >=0){
      return true;
    }
    //Apicloud中的$.ajax请求
    reqWith = request.getHeader("x-requested-with");
    if(reqWith != null && reqWith.toLowerCase().indexOf("com.apicloud.apploader") >=0){
      return true;
    }
    
    //然后验证Accept参数
    String accept = request.getHeader("Accept");
    if(StringUtils.isBlank(accept)) {
      accept = request.getParameter("Accept");
      if(StringUtils.isBlank(accept)) {
        return false;
      }
    }

    accept = accept.toLowerCase();
    return (accept.indexOf("x-json") >= 0);    
  }
  
  /**
   * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
   * 
   * 返回的结果的Parameter名已去除前缀.
   */
  @SuppressWarnings("rawtypes")
  public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
    Assert.notNull(request, "Request must not be null");
    Enumeration paramNames = request.getParameterNames();
    Map<String, Object> params = new TreeMap<String, Object>();
    if (prefix == null) {
      prefix = "";
    }
    while (paramNames != null && paramNames.hasMoreElements()) {
      String paramName = (String) paramNames.nextElement();
      if ("".equals(prefix) || paramName.startsWith(prefix)) {
        String unprefixed = paramName.substring(prefix.length());
        String[] values = request.getParameterValues(paramName);
        if (values == null || values.length == 0) {
          // Do nothing, no values found at all.
        } else if (values.length > 1) {
          params.put(unprefixed, values);
        } else {
          params.put(unprefixed, values[0]);
        }
      }
    }
    return params;
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
   * 设置让浏览器弹出下载对话框的Header.
   * 
   * @param fileName 下载后的文件名.
   */
  public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
    setFileDownloadHeader(response, fileName, "ISO8859-1");
  }
  
  /**
   * 设置让浏览器弹出下载对话框的Header.
   * 
   * @param fileName 下载后的文件名.
   */
  public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String encoding) {
    try {
      // 中文文件名支持
      String encodedfileName = new String(fileName.getBytes(), encoding);
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
    } catch (UnsupportedEncodingException e) {
    }
  }
  

  /**
   * 设置LastModified Header.
   */
  public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
    response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
  }

  /**
   * 设置Etag Header.
   */
  public static void setEtag(HttpServletResponse response, String etag) {
    response.setHeader(HttpHeaders.ETAG, etag);
  }

  /**
   * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
   * 
   * 如果无修改, checkIfModify返回false ,设置304 not modify status.
   * 
   * @param lastModified 内容的最后修改时间.
   */
  public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
      long lastModified) {
    long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
    if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return false;
    }
    return true;
  }

  /**
   * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
   * 
   * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
   * 
   * @param etag 内容的ETag.
   */
  public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
    String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
    if (headerValue != null) {
      boolean conditionSatisfied = false;
      if (!"*".equals(headerValue)) {
        StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

        while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
          String currentToken = commaTokenizer.nextToken();
          if (currentToken.trim().equals(etag)) {
            conditionSatisfied = true;
          }
        }
      } else {
        conditionSatisfied = true;
      }

      if (conditionSatisfied) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        response.setHeader(HttpHeaders.ETAG, etag);
        return false;
      }
    }
    return true;
  }
  
  private RequestUtils() {
  }
}
