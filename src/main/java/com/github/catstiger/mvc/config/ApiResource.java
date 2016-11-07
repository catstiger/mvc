package com.github.catstiger.mvc.config;

import java.lang.reflect.Method;

/**
 * ApiResource用于表达一个URI及其对应的Service和Method
 * @author catstiger
 *
 */
public class ApiResource {
  private String uri;
  private String uriPrefix;
  private String uriSufix;
  private String providerType;
  private String serviceId;
  private String methodName;
  private Object serviceInstance;
  private Method method;
  private Boolean singleton = true;
  
  /**
   * 对应的URI，MVC系统根据URI找到处理URI的对象和方法
   * @return
   */
  public String getUri() {
    return uri;
  }
  
  public void setUri(String uri) {
    this.uri = uri;
  }
  
  /**
   * 返回处理改URI的处理器的生成方式。
   * @see {@link com.github.catstiger.mvc.service.ServiceProvider#SERVICE_PROVIDER_SPRING}
   * @see {@link com.github.catstiger.mvc.service.ServiceProvider#SERVICE_PROVIDER_CREATE}
   */
  public String getProviderType() {
    return providerType;
  }
  
  public void setProviderType(String providerType) {
    this.providerType = providerType;
  }
  
  /**
   * 如果service生成方式为spring,那么返回spring中得到bean id
   * 如果service生成方式为create,那么返回该对象的全限定类名
   */
  public String getServiceId() {
    return serviceId;
  }
  
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }
  
  /**
   * 处理该URI的service的实例
   */
  public Object getServiceInstance() {
    return serviceInstance;
  }
  
  public void setServiceInstance(Object service) {
    this.serviceInstance = service;
  }
  /**
   * 处理该URI的方法Method对象
   */
  public Method getMethod() {
    return method;
  }
  
  public void setMethod(Method method) {
    this.method = method;
  }
  
  /**
   * 处理该URI的方法名
   */
  public String getMethodName() {
    return methodName;
  }
  
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Boolean getSingleton() {
    return singleton;
  }

  public void setSingleton(Boolean singleton) {
    this.singleton = singleton;
  }

  public String getUriPrefix() {
    return uriPrefix;
  }

  public void setUriPrefix(String uriPrefix) {
    this.uriPrefix = uriPrefix;
  }

  public String getUriSufix() {
    return uriSufix;
  }

  public void setUriSufix(String uriSufix) {
    this.uriSufix = uriSufix;
  }
}
