package com.github.catstiger.mvc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.github.catstiger.mvc.SpringHelper;
import com.github.catstiger.mvc.config.ApiResource;

public class ServiceProviderSpring implements ServiceProvider {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Override
  public Object getService(ApiResource apiRes) {
    ApplicationContext ctx = SpringHelper.getApplicationContext();
    if(ctx == null) {
      throw new RuntimeException("No application context found.");
    }
    Object service = ctx.getBean(apiRes.getServiceId());
    logger.debug("Get service from spring [{}]", service.getClass());
    return service;
  }

}
