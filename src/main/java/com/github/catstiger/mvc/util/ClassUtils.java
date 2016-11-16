package com.github.catstiger.mvc.util;

import java.util.HashMap;
import java.util.Map;


public final class ClassUtils {
  private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
  static {
       primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
       primitiveWrapperMap.put(Byte.TYPE, Byte.class);
       primitiveWrapperMap.put(Character.TYPE, Character.class);
       primitiveWrapperMap.put(Short.TYPE, Short.class);
       primitiveWrapperMap.put(Integer.TYPE, Integer.class);
       primitiveWrapperMap.put(Long.TYPE, Long.class);
       primitiveWrapperMap.put(Double.TYPE, Double.class);
       primitiveWrapperMap.put(Float.TYPE, Float.class);
       primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
  }
  
  /** The CGLIB class separator character "$$" */
  public static final String CGLIB_CLASS_SEPARATOR = "$$";

  /**
   * Maps wrapper {@code Class}es to their corresponding primitive types.
   */
  private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
  static {
      for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
          Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
          if (!primitiveClass.equals(wrapperClass)) {
              wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
          }
      }
  }
  
  private ClassUtils() {
    
  }
  
  public static Class<?> getUserClass(Class<?> clazz) {
    if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
      Class<?> superclass = clazz.getSuperclass();
      if (superclass != null && Object.class != superclass) {
        return superclass;
      }
    }
    return clazz;
  }
  
  public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
    return isAssignable(cls, toClass, false);  
  }
  
  public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
    if (toClass == null) {
        return false;
    }
    // have to check for null, as isAssignableFrom doesn't
    if (cls == null) {
        return !toClass.isPrimitive();
    }
    //autoboxing:
    if (autoboxing) {
        if (cls.isPrimitive() && !toClass.isPrimitive()) {
            cls = primitiveToWrapper(cls);
            if (cls == null) {
                return false;
            }
        }
        if (toClass.isPrimitive() && !cls.isPrimitive()) {
            cls = wrapperToPrimitive(cls);
            if (cls == null) {
                return false;
            }
        }
    }
    if (cls.equals(toClass)) {
        return true;
    }
    if (cls.isPrimitive()) {
        if (toClass.isPrimitive() == false) {
            return false;
        }
        if (Integer.TYPE.equals(cls)) {
            return Long.TYPE.equals(toClass)
                || Float.TYPE.equals(toClass)
                || Double.TYPE.equals(toClass);
        }
        if (Long.TYPE.equals(cls)) {
            return Float.TYPE.equals(toClass)
                || Double.TYPE.equals(toClass);
        }
        if (Boolean.TYPE.equals(cls)) {
            return false;
        }
        if (Double.TYPE.equals(cls)) {
            return false;
        }
        if (Float.TYPE.equals(cls)) {
            return Double.TYPE.equals(toClass);
        }
        if (Character.TYPE.equals(cls)) {
            return Integer.TYPE.equals(toClass)
                || Long.TYPE.equals(toClass)
                || Float.TYPE.equals(toClass)
                || Double.TYPE.equals(toClass);
        }
        if (Short.TYPE.equals(cls)) {
            return Integer.TYPE.equals(toClass)
                || Long.TYPE.equals(toClass)
                || Float.TYPE.equals(toClass)
                || Double.TYPE.equals(toClass);
        }
        if (Byte.TYPE.equals(cls)) {
            return Short.TYPE.equals(toClass)
                || Integer.TYPE.equals(toClass)
                || Long.TYPE.equals(toClass)
                || Float.TYPE.equals(toClass)
                || Double.TYPE.equals(toClass);
        }
        // should never get here
        return false;
    }
    return toClass.isAssignableFrom(cls);
  }
  
  /**
   * <p>Converts the specified primitive Class object to its corresponding
   * wrapper Class object.</p>
   *
   * <p>NOTE: From v2.2, this method handles {@code Void.TYPE},
   * returning {@code Void.TYPE}.</p>
   *
   * @param cls  the class to convert, may be null
   * @return the wrapper class for {@code cls} or {@code cls} if
   * {@code cls} is not a primitive. {@code null} if null input.
   * @since 2.1
   */
  public static Class<?> primitiveToWrapper(Class<?> cls) {
      Class<?> convertedClass = cls;
      if (cls != null && cls.isPrimitive()) {
          convertedClass = primitiveWrapperMap.get(cls);
      }
      return convertedClass;
  }
  
  /**
   * <p>Converts the specified wrapper class to its corresponding primitive
   * class.</p>
   *
   * <p>This method is the counter part of {@code primitiveToWrapper()}.
   * If the passed in class is a wrapper class for a primitive type, this
   * primitive type will be returned (e.g. {@code Integer.TYPE} for
   * {@code Integer.class}). For other classes, or if the parameter is
   * <b>null</b>, the return value is <b>null</b>.</p>
   *
   * @param cls the class to convert, may be <b>null</b>
   * @return the corresponding primitive type if {@code cls} is a
   * wrapper class, <b>null</b> otherwise
   * @see #primitiveToWrapper(Class)
   * @since 2.4
   */
  public static Class<?> wrapperToPrimitive(Class<?> cls) {
      return wrapperPrimitiveMap.get(cls);
  }
  
  /**
   * Check whether the given object is a CGLIB proxy.
   * @param object the object to check
   * @see org.springframework.aop.support.AopUtils#isCglibProxy(Object)
   */
  public static boolean isCglibProxy(Object object) {
    return isCglibProxyClass(object.getClass());
  }

  /**
   * Check whether the specified class is a CGLIB-generated class.
   * @param clazz the class to check
   */
  public static boolean isCglibProxyClass(Class<?> clazz) {
    return (clazz != null && isCglibProxyClassName(clazz.getName()));
  }

  /**
   * Check whether the specified class name is a CGLIB-generated class.
   * @param className the class name to check
   */
  public static boolean isCglibProxyClassName(String className) {
    return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
  }
  
  /**
   * Check whether the given class is cache-safe in the given context,
   * i.e. whether it is loaded by the given ClassLoader or a parent of it.
   * @param clazz the class to analyze
   * @param classLoader the ClassLoader to potentially cache metadata in
   */
  public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader) {
    try {
      ClassLoader target = clazz.getClassLoader();
      if (target == null) {
        return true;
      }
      ClassLoader cur = classLoader;
      if (cur == target) {
        return true;
      }
      while (cur != null) {
        cur = cur.getParent();
        if (cur == target) {
          return true;
        }
      }
      return false;
    }
    catch (SecurityException ex) {
      // Probably from the system ClassLoader - let's consider it safe.
      return true;
    }
  }
}
