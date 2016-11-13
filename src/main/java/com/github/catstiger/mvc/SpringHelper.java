package com.github.catstiger.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public final class SpringHelper {
  private static Logger logger = LoggerFactory.getLogger(SpringHelper.class);
  
  private static ApplicationContext applicationContext;
  
  private SpringHelper() {
    
  }
  
  public static ApplicationContext getApplicationContext() {
    if(applicationContext == null) {
      logger.warn("There is no application context!");
    }
    return applicationContext;
  }
  
  public synchronized static void initApplicationContext(ApplicationContext ctx) {
    if(applicationContext == null) {
      logger.info("There is no application context detected.");
    }
    applicationContext = ctx;
  }
  
}
