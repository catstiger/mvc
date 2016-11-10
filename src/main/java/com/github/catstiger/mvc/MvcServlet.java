package com.github.catstiger.mvc;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.github.catstiger.mvc.config.Initializer;

public class MvcServlet extends HttpServlet {
  private static Logger logger = LoggerFactory.getLogger(MvcServlet.class);

  @SuppressWarnings("unchecked")
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.debug("URL {}", request.getRequestURL());
    logger.debug("URI {}", request.getRequestURI());
    logger.debug("ACTION {}", request.getRequestURI().replace(request.getContextPath(), ""));
    
    ServletObjectHolder.setRequest(request);
    ServletObjectHolder.setResponse(response);
    ServletObjectHolder.setRequestParameters(request.getParameterMap());
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    logger.debug("ServletContextName {}", config.getServletContext().getServletContextName());
    logger.debug("getServletContextName {}", config.getServletContext().getServletContextName());
    logger.debug("getContextPath {}", config.getServletContext().getContextPath());
    try {
      logger.debug("getContextPath {}", config.getServletContext().getResource("/"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    
    //Init Spring appliction context
    ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    SpringHelper.initApplicationContext(applicationContext);
    
    Initializer initializer = Initializer.getInstance();
    initializer.loadApiResources(config);
  }

}
