package com.github.catstiger.mvc.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides a helper that locates the declarated generics type of a class.
 * 
 */
@SuppressWarnings("rawtypes")
public final class GenericsUtils {
  /**
   * prevent from initializing
   */
  private GenericsUtils() {
  }

  /**
   * Locates the first generic declaration on a class.
   * 
   * @param clazz The class to introspect
   * @return the first generic declaration, or <code>null</code> if cannot be
   *         determined
   */
  public static Class getGenericClassSupper(Class clazz) {
    return getGenericClassSupper(clazz, 0);
  }
  
  

  /**
   * Locates generic declaration by index on a class.
   * 
   * @param clazz clazz The class to introspect
   * @param index the Index of the generic ddeclaration,start from 0.
   */
  public static Class getGenericClassSupper(Class clazz, int index) {
    Type genType = clazz.getGenericSuperclass();
    

    if (genType instanceof ParameterizedType) {
      Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

      if ((params != null) && (params.length >= (index - 1))) {
        return (Class) params[index];
      }
    }
    return null;
  }
  
  /**
   * 得到给出的类，所实现的接口的泛型类型，目前只支持单接口实现
   * @param clazz 给出类
   * @param index 给出接口泛型参数的索引
   */
  public static Class getCenericClassInterface(Class clazz, int index) {
    Type[] types = clazz.getGenericInterfaces();
    if(types == null || types.length == 0 || index > types.length - 1) {
      return null;
    }
    if(types.length > 1) {
      throw new UnsupportedOperationException("只支持实现了一个接口的情况");
    }
    
    Type genType = types[0];
    
    if (genType instanceof ParameterizedType) {
      Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

      if ((params != null) && (params.length >= (index - 1))) {
        return (Class) params[index];
      }
    }
    return null;
  }
  
  public static Class getGenericClass(Class clazz, int index) {
    Class genericType = getGenericClassSupper(clazz, index);
    if(genericType == null) {
      genericType = getCenericClassInterface(clazz, index);
    }
    return genericType;
  }
}
