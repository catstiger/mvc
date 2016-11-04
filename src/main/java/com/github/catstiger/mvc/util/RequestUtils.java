package com.github.catstiger.mvc.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class RequestUtils {
  /**
   * Return an appropriate request object of the specified type, if available,
   * unwrapping the given request as far as necessary.
   * @param request the servlet request to introspect
   * @param requiredType the desired type of request object
   * @return the matching request object, or {@code null} if none
   * of that type is available
   */
  @SuppressWarnings("unchecked")
  public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType) {
    if (requiredType != null) {
      if (requiredType.isInstance(request)) {
        return (T) request;
      }
      else if (request instanceof ServletRequestWrapper) {
        return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
      }
    }
    return null;
  }
  
  /**
   * Return an appropriate response object of the specified type, if available,
   * unwrapping the given response as far as necessary.
   * @param response the servlet response to introspect
   * @param requiredType the desired type of response object
   * @return the matching response object, or {@code null} if none
   * of that type is available
   */
  @SuppressWarnings("unchecked")
  public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType) {
    if (requiredType != null) {
      if (requiredType.isInstance(response)) {
        return (T) response;
      }
      else if (response instanceof ServletResponseWrapper) {
        return getNativeResponse(((ServletResponseWrapper) response).getResponse(), requiredType);
      }
    }
    return null;
  }
  
  /**
   * Retrieve the first cookie with the given name. Note that multiple
   * cookies can have the same name but different paths or domains.
   * @param request current servlet request
   * @param name cookie name
   * @return the first cookie with the given name, or {@code null} if none is found
   */
  public static Cookie getCookie(HttpServletRequest request, String name) {
    if(request == null) {
      throw new java.lang.IllegalArgumentException("Request must not be null.");
    }
    
    Cookie cookies[] = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie;
        }
      }
    }
    return null;
  }
  
  /**
   * Obtain a named parameter from the given request parameters.
   * <p>See {@link #findParameterValue(java.util.Map, String)}
   * for a description of the lookup algorithm.
   * @param request current HTTP request
   * @param name the <i>logical</i> name of the request parameter
   * @return the value of the parameter, or {@code null}
   * if the parameter does not exist in given request
   */
  @SuppressWarnings("unchecked")
  public static String findParameterValue(ServletRequest request, String name) {
    return findParameterValue(request.getParameterMap(), name);
  }

  /**
   * Obtain a named parameter from the given request parameters.
   * <p>This method will try to obtain a parameter value using the
   * following algorithm:
   * <ol>
   * <li>Try to get the parameter value using just the given <i>logical</i> name.
   * This handles parameters of the form <tt>logicalName = value</tt>. For normal
   * parameters, e.g. submitted using a hidden HTML form field, this will return
   * the requested value.</li>
   * <li>Try to obtain the parameter value from the parameter name, where the
   * parameter name in the request is of the form <tt>logicalName_value = xyz</tt>
   * with "_" being the configured delimiter. This deals with parameter values
   * submitted using an HTML form submit button.</li>
   * <li>If the value obtained in the previous step has a ".x" or ".y" suffix,
   * remove that. This handles cases where the value was submitted using an
   * HTML form image button. In this case the parameter in the request would
   * actually be of the form <tt>logicalName_value.x = 123</tt>. </li>
   * </ol>
   * @param parameters the available parameter map
   * @param name the <i>logical</i> name of the request parameter
   * @return the value of the parameter, or {@code null}
   * if the parameter does not exist in given request
   */
  public static String findParameterValue(Map<String, ?> parameters, String name) {
    // First try to get it as a normal name=value parameter
    Object value = parameters.get(name);
    if (value instanceof String[]) {
      String[] values = (String[]) value;
      return (values.length > 0 ? values[0] : null);
    } else if (value != null) {
      return value.toString();
    }
    
    // We couldn't find the parameter value...
    return null;
  }

  /**
   * Return a map containing all parameters with the given prefix.
   * Maps single values to String and multiple values to String array.
   * <p>For example, with a prefix of "spring_", "spring_param1" and
   * "spring_param2" result in a Map with "param1" and "param2" as keys.
   * @param request HTTP request in which to look for parameters
   * @param prefix the beginning of parameter names
   * (if this is null or the empty string, all parameters will match)
   * @return map containing request parameters <b>without the prefix</b>,
   * containing either a String or a String array as values
   * @see javax.servlet.ServletRequest#getParameterNames
   * @see javax.servlet.ServletRequest#getParameterValues
   * @see javax.servlet.ServletRequest#getParameterMap
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
    if(request == null) {
      throw new java.lang.IllegalArgumentException("Request must not be null");
    }
    Enumeration<String> paramNames = request.getParameterNames();
    Map<String, Object> params = new TreeMap<>();
    if (prefix == null) {
      prefix = "";
    }
    while (paramNames != null && paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      if ("".equals(prefix) || paramName.startsWith(prefix)) {
        String unprefixed = paramName.substring(prefix.length());
        String[] values = request.getParameterValues(paramName);
        if (values == null || values.length == 0) {
          // Do nothing, no values found at all.
        }
        else if (values.length > 1) {
          params.put(unprefixed, values);
        }
        else {
          params.put(unprefixed, values[0]);
        }
      }
    }
    return params;
  }
  
  private RequestUtils() {
  }
}
