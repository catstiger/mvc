package com.github.catstiger.mvc.service;

import org.springframework.web.context.WebApplicationContext;

import com.github.catstiger.mvc.config.ApiResource;

public class ServiceProviderSpring implements ServiceProvider {
  private WebApplicationContext ctx;

  @Override
  public Object getService(ApiResource apiRes) {
    if(ctx == null) {
      throw new RuntimeException("No application context found.");
    }
    return ctx.getBean(apiRes.getServiceId());
  }

  public WebApplicationContext getCtx() {
    return ctx;
  }

  public void setCtx(WebApplicationContext ctx) {
    this.ctx = ctx;
  }

}
