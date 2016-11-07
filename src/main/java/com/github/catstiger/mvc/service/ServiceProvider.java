package com.github.catstiger.mvc.service;

import com.github.catstiger.mvc.config.ApiResource;

public interface ServiceProvider {
  /**
   * 如果不是Spring管理的对象，则创建该对象的实例，要求改对象是一个“非可变”的单例对象
   */
  public static final String SERVICE_PROVIDER_CREATE = "_create_";
  /**
   * 处理器为spring管理的bean
   */
  public static final String SERVICE_PROVIDER_SPRING = "_spring_";
  
  /**
   * 根据Service ID，得到Service的实例。如果Service产生方式为#SERVICE_GENERATOR_SPRING,
   * serviceId为Spring中的beanId;如果service的产生方式为{@link #SERVICE_GENERATOR_CREATE}
   * 则serviceId为Class Name，此时需要调用该Class缺省构造函数创建之。
   * @param apiResource ApiResource that contains service id...
   * @return Instance of the service.
   * @throws RuntimeException occurs will none service found.
   */
  public abstract Object getService(ApiResource apiResource);
}
