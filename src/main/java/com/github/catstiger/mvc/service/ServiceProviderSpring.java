package com.github.catstiger.mvc.service;

import org.springframework.context.ApplicationContext;

import com.github.catstiger.mvc.SpringHelper;
import com.github.catstiger.mvc.config.ApiResource;

public class ServiceProviderSpring implements ServiceProvider {
  @Override
  public Object getService(ApiResource apiRes) {
    ApplicationContext ctx = SpringHelper.getApplicationContext();
    if(ctx == null) {
      throw new RuntimeException("No application context found.");
    }
    Object service = ctx.getBean(apiRes.getServiceId());
    
    return service;
  }

}
