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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.resolver.RequestParser;
import com.github.catstiger.mvc.resolver.ResolverFactory;
import com.github.catstiger.mvc.resolver.ResponseResolver;
import com.github.catstiger.mvc.resolver.ServiceInvoker;
import com.github.catstiger.mvc.util.RequestUtils;
import com.github.catstiger.mvc.util.ValueMapUtils;

public class MvcFilter implements Filter {
  private static Logger logger = LoggerFactory.getLogger(MvcFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    
    boolean isGet = "GET".equals(req.getMethod());
    if(isGet) {
      Initializer init = Initializer.getInstance();
      if(init.getCacheSeconds() == 0L) {
        RequestUtils.setNoCacheHeader(resp);
      } else {
        RequestUtils.setExpiresHeader(resp, init.getCacheSeconds());  
      }
    }
    
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
        ResponseResolver resolver = null;
        Object value;
        try { //调用服务并获取ResponseResolver对象
          Map<String, Object> cascadedMap = ValueMapUtils.inheritableParams(request.getParameterMap());
          value = ServiceInvoker.invoke(apiRes, cascadedMap); 
          resolver = ResolverFactory.getSuccessResolver(request);
        } catch (Exception e) { //错误处理
          logger.error(e.getMessage());
          e.printStackTrace();
          resolver = ResolverFactory.getFailureResolver(request);
          value = e;
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
