package com.github.catstiger.mvc.config;

import java.lang.reflect.Method;

/**
 * ApiResource用于表达一个URI及其对应的Service和Method
 * @author catstiger
 *
 */
public class ApiResource {
  /**
   * 如果不是Spring管理的对象，则创建该对象的实例，要求改对象是一个“非可变”的单例对象
   */
  public static final String SERVICE_GENERATOR_CREATE = "_create_";
  /**
   * 处理器为spring管理的bean
   */
  public static final String SERVICE_GENERATOR_SPRING = "_spring_";
  
  private String uri;
  private String serviceGenerator;
  private String serviceId;
  private String methodName;
  private Object service;
  private Method method;
  
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
   * @see #SERVICE_GENERATOR_CREATE
   * @see #SERVICE_GENERATOR_SPRING
   */
  public String getServiceGenerator() {
    return serviceGenerator;
  }
  
  public void setServiceGenerator(String serviceGenerator) {
    this.serviceGenerator = serviceGenerator;
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
  public Object getService() {
    return service;
  }
  
  public void setService(Object service) {
    this.service = service;
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
}
