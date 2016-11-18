package com.github.catstiger.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;

public interface ResponseResolver {
  /**
   * HTTP缓存时间
   */
  public static final long CACHE_EXPIRES_SEC = 86400L;
  
  /**
   * URI调用完成之后，调用这个方法，处理后续的工作
   * <ul>
   *     <li>如果调用没有找到ApiResource, 处理404错误</li>
   *     <li>如果调用过程出现异常，处理505错误</li>
   *     <li>如果根据uri判断为JSON请求，则将结果转换为JSON</li>
   *     <li>如果根据uri判断为HTML情况，则重定向到相关页面</li>
   * </ul>
   * @param request HTTP请求
   * @param HttpServletResponse
   * @param apiResource 本次调用对应的ApiResource对象
   * @param value 本次调用处理的数据
   */
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value);
  
  /**
   * This marker class is only to be used with annotations, to
   * indicate that <b>no resolver is to be used</b>.
   */
  public abstract static class None
  implements ResponseResolver { }
}
