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

public class MvcFilter implements Filter {
  private static Logger logger = LoggerFactory.getLogger(MvcFilter.class);

  @SuppressWarnings("unchecked")
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    
    RequestObjectHolder.setRequest(req);
    RequestObjectHolder.setResponse(resp);
    RequestObjectHolder.setRequestParameters(req.getParameterMap());
    logger.debug(req.getRequestURI());
    String serviceUri = RequestParser.getRequestUri(req);
    ApiResource apiRes = ApiResHolder.getInstance().getApiResource(serviceUri);
    if(apiRes != null) {
      doService(req, resp, apiRes);
    } 
    else if (RequestParser.isStatic(req.getRequestURI())) {
      chain.doFilter(request, response);
    } 
    else { //找不到对应的Service，404错误
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
  
  private void doService(HttpServletRequest req, HttpServletResponse resp, ApiResource apiRes) {
    try {
      Map<String, Object> cascadedMap = RequestObjectHolder.getInheritableParams();
      Object value = ServiceInvoker.invoke(apiRes, cascadedMap);
      ResponseResolver resolver = ResolverFactory.getSuccessResolver(req);
      if(resolver != null) {
        resolver.resolve(req, resp, apiRes, value);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
      ResponseResolver resolver = ResolverFactory.getFailureResolver(req);
      if(resolver != null) {
        resolver.resolve(req, resp, apiRes, e);
      }
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
