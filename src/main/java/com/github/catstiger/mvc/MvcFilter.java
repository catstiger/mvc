package com.github.catstiger.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.resolver.ResolverFactory;
import com.github.catstiger.mvc.resolver.ResponseResolver;
import com.github.catstiger.mvc.service.RequestParser;
import com.github.catstiger.mvc.service.ServiceInvoker;
import com.github.catstiger.utils.RequestUtils;
import com.github.catstiger.utils.ValueMapUtils;

public class MvcFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    doFilterInternal(req, resp, chain);
  }
  
  @SuppressWarnings("unchecked")
  private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    //保存本次请求的参数
    RequestObjectHolder.clear();
    RequestObjectHolder.setRequest(request);
    RequestObjectHolder.setResponse(response);
    
    try {
      //本次请求对应的ApiResource对象
      String serviceUri = RequestParser.getRequestUri(request);
      ApiResource apiRes = ApiResHolder.getInstance().getApiResource(serviceUri);
  
      if(apiRes != null) {
        boolean isGet = "GET".equals(request.getMethod());
        //在GET方式下，尝试提供HTTP缓存支持
        if(isGet) {
          Initializer init = Initializer.getInstance();
          if(init.getCacheSeconds() == 0L) {
            RequestUtils.setNoCacheHeader(response);
          } else {
            RequestUtils.setExpiresHeader(response, init.getCacheSeconds());  
          }
        }
        
        ResponseResolver resolver = null;
        Object value;
        try { //调用服务并获取ResponseResolver对象
          Map<String, Object> cascadedMap = ValueMapUtils.inheritableParams(request.getParameterMap());
          value = ServiceInvoker.invoke(apiRes, cascadedMap); 
          resolver = ResolverFactory.getSuccessResolver(request);
        } catch (Exception e) {
          resolver = ResolverFactory.getFailureResolver(request);
          value = e.getCause();
        } 
        
        //执行ResponseResolver
        if(resolver != null && resolver.getClass() != ResponseResolver.None.class) {
          resolver.resolve(request, response, apiRes, value);
        } else {
          chain.doFilter(request, response);
        }
        
      } else {
        chain.doFilter(request, response);
      }
    } finally {
      RequestObjectHolder.clear();
    }
    
  }
  

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
    SpringHelper.initApplicationContext(applicationContext);
    Initializer initializer = Initializer.getInstance();

    String basePackage = filterConfig.getInitParameter(Initializer.INIT_PARAM_BASE_PACKAGE);
    //加载各种初始化参数
    initializer.initParams(filterConfig);
    //加载URI服务资源
    initializer.loadApiResources(basePackage);
  }

  @Override
  public void destroy() {
    
  }

}
