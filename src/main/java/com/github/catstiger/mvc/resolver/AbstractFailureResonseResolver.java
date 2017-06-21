package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.exception.ReadableException;

public abstract class AbstractFailureResonseResolver extends AbstractResponseResolver {

  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    if(value != null) {
      if(value instanceof ReadableException) { //如果是可读异常，说明开发者抛出此异常的目的是通知用户，因此，需要渲染至View层
        ReadableException e = ((ReadableException) value);
        handleReadableException(request, response, apiResource, e);
      } 
      else if (value instanceof Throwable) { //如果不是可读异常，则发送505错误
        Throwable ex = (Throwable) value;
        ex.printStackTrace();
        handleUnexpectException(request, response, apiResource, ex);
      } 
      else {
        handleReadableException(request, response, apiResource, value.toString());
      }
    } else {
      logger.error(apiResource.toString());
      handleReadableException(request, response, apiResource, "未知错误！");
    } 
  }

  protected void handleReadableException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, ReadableException e) {
    handleReadableException(request, response, apiResource, e.getMessage());
  }

  /**
   * 处理可读异常，通常可读异常是开发者可以预见的，并且用户可以处理的异常，通常用于通知用户执行正确的操作
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param apiResource ApiResource
   * @param string 异常信息
   */
  protected abstract void handleReadableException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, String string);
  /**
   * 处理不可读异常，不可读异常通常由系统抛出，用户无法处理
   */
  protected abstract void handleUnexpectException(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Throwable ex);
}
