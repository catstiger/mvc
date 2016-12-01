package com.github.catstiger.mvc.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;

import com.github.catstiger.mvc.annotation.Param;

public final class ReflectUtils {
  /**
   * Naming prefix for CGLIB-renamed methods.
   * 
   * @see #isCglibRenamedMethod
   */
  private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";

  private static final Method[] NO_METHODS = {};

  private static final Field[] NO_FIELDS = {};
  
  private static final PropertyDescriptor[] NO_PROPERTY_DESCRIPTERS = {};

  /**
   * Cache for {@link Class#getDeclaredMethods()} plus equivalent default
   * methods from Java 8 based interfaces, allowing for fast iteration.
   */
  private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentHashMap<Class<?>, Method[]>(256);

  /**
   * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
   */
  private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<Class<?>, Field[]>(256);
  
  private static final Map<Class<?>, Field[]> fieldsCache = new ConcurrentHashMap<Class<?>, Field[]>(256);
  
  
  private static final Map<Class<?>, PropertyDescriptor[]> propertyDescriptorCache = new ConcurrentHashMap<Class<?>, PropertyDescriptor[]>(256);

  /**
   * Convenience method to instantiate a class using its no-arg constructor.
   * As this method doesn't try to load classes by name, it should avoid
   * class-loading issues.
   * @param clazz class to instantiate
   * @return the new instance
   * @throws RuntimeException if the bean cannot be instantiated
   */
  public static <T> T instantiate(Class<T> clazz) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    
    if (clazz.isInterface()) {
      throw new RuntimeException("Specified class is an interface [" + clazz.getName() + "]");
    }
    try {
      return clazz.newInstance();
    }
    catch (InstantiationException ex) {
      throw new RuntimeException("Specified class is an abstract class? [" + clazz.getName() + "]", ex);
    }
    catch (IllegalAccessException ex) {
      throw new RuntimeException("Is the constructor accessible? [" + clazz.getName() + "]", ex);
    }
  }
  
  /**
   * Retrieve the JavaBeans {@code PropertyDescriptor}s of a given class.
   * @param clazz the Class to retrieve the PropertyDescriptors for
   * @return an array of {@code PropertyDescriptors} for the given class
   * @throws BeansException if PropertyDescriptor look fails
   */
  public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    
    if(propertyDescriptorCache.containsKey(clazz)) {
      return propertyDescriptorCache.get(clazz);
    }
    
    Field[] fields = getFields(clazz);
    if(fields == null || fields.length == 0) {
      return NO_PROPERTY_DESCRIPTERS;
    }
    
    PropertyDescriptor[] pds = new PropertyDescriptor[fields.length];
    try {
      for(int i = 0; i < fields.length; i++) {
        PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), clazz);
        pds[i] = pd;
      }
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
    propertyDescriptorCache.put(clazz, pds);
    
    
    return pds;
  }
  
  /**
   * Attempt to find a {@link Field field} on the supplied {@link Class} with the
   * supplied {@code name}. Searches all superclasses up to {@link Object}.
   * @param clazz the class to introspect
   * @param name the name of the field
   * @return the corresponding Field object, or {@code null} if not found
   */
  public static Field findField(Class<?> clazz, String name) {
    return findField(clazz, name, null);
  }
  
  public static Field findField(Class<?> clazz, String name, Class<?> type) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    if(name == null || type == null) {
      throw new RuntimeException("Either name or type of the field must be specified");
    }
    
    Class<?> searchType = clazz;
    while (Object.class != searchType && searchType != null) {
      Field[] fields = getDeclaredFields(searchType);
      for (Field field : fields) {
        if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
          return field;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }
  
  /**
   * Returns an array of {@code Field} objects reflecting all the fields
   * declared by the class or interface represented by this
   * {@code Class} object. This includes public, protected, default
   * (package) access, and private fields, and includes inherited fields.
   * @param clazz
   * @return  the LinkedList of {@code Field} objects representing all the
     *          declared fields of this class
   */
  public static  Field[] getFields(Class<?> clazz) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    
    if(fieldsCache.containsKey(clazz)) {
      return fieldsCache.get(clazz);
    }
    
    List<Field> fieldList = fields(clazz);
    Field[] fields = fieldList.toArray(new Field[]{});
    
    fieldsCache.put(clazz, fields);
    return fields;
  }
  
  /**
   * Set the field represented by the supplied {@link Field field object} on the
   * specified {@link Object target object} to the specified {@code value}.
   * In accordance with {@link Field#set(Object, Object)} semantics, the new value
   * is automatically unwrapped if the underlying field has a primitive type.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
   * @param field the field to set
   * @param target the target object on which to set the field
   * @param value the value to set (may be {@code null})
   */
  public static void setField(Field field, Object target, Object value) {
    try {
      field.set(target, value);
    }
    catch (IllegalAccessException ex) {
      throw new IllegalStateException(
          "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
    }
  }

  /**
   * Get the field represented by the supplied {@link Field field object} on the
   * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
   * semantics, the returned value is automatically wrapped if the underlying field
   * has a primitive type.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
   * @param field the field to get
   * @param target the target object from which to get the field
   * @return the field's current value
   */
  public static Object getField(Field field, Object target) {
    try {
      return field.get(target);
    }
    catch (IllegalAccessException ex) {
      throw new IllegalStateException(
          "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
    }
  }

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name
   * and no parameters. Searches all superclasses up to {@code Object}.
   * <p>Returns {@code null} if no {@link Method} can be found.
   * @param clazz the class to introspect
   * @param name the name of the method
   * @return the Method object, or {@code null} if none found
   */
  public static Method findMethod(Class<?> clazz, String name) {
    return findMethod(clazz, name, new Class<?>[0]);
  }

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name
   * and parameter types. Searches all superclasses up to {@code Object}.
   * <p>Returns {@code null} if no {@link Method} can be found.
   * @param clazz the class to introspect
   * @param name the name of the method
   * @param paramTypes the parameter types of the method
   * (may be {@code null} to indicate any signature)
   * @return the Method object, or {@code null} if none found
   */
  public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    if(name == null) {
      throw new RuntimeException("Method name must not be null");
    }
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
      for (Method method : methods) {
        if (name.equals(method.getName()) &&
            (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with no arguments.
   * The target object can be {@code null} when invoking a static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @return the invocation result, if any
   * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
   */
  public static Object invokeMethod(Method method, Object target) {
    return invokeMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with the
   * supplied arguments. The target object can be {@code null} when invoking a
   * static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   */
  public static Object invokeMethod(Method method, Object target, Object... args) {
    try {
      return method.invoke(target, args);
    }
    catch (Exception ex) {
      handleReflectionException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  /**
   * Handle the given reflection exception. Should only be called if no
   * checked exception is expected to be thrown by the target method.
   * <p>Throws the underlying RuntimeException or Error in case of an
   * InvocationTargetException with such a root cause. Throws an
   * IllegalStateException with an appropriate message else.
   * @param ex the reflection exception to handle
   */
  public static void handleReflectionException(Exception ex) {
    if (ex instanceof NoSuchMethodException) {
      throw new IllegalStateException("Method not found: " + ex.getMessage());
    }
    if (ex instanceof IllegalAccessException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof InvocationTargetException) {
      Throwable targetEx = ((InvocationTargetException) ex).getTargetException();
      String msg = null;
      if(targetEx != null) {
        msg = targetEx.getMessage();
        new RuntimeException(msg, targetEx); 
      }
      new RuntimeException(ex.getMessage(), ex);
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new RuntimeException(ex.getMessage(), ex);
  }
  
  /**
   * Determine whether the given method is a CGLIB 'renamed' method,
   * following the pattern "CGLIB$methodName$0".
   * @param renamedMethod the method to check
   * @see org.springframework.cglib.proxy.Enhancer#rename
   */
  public static boolean isCglibRenamedMethod(Method renamedMethod) {
    String name = renamedMethod.getName();
    if (name.startsWith(CGLIB_RENAMED_METHOD_PREFIX)) {
      int i = name.length() - 1;
      while (i >= 0 && Character.isDigit(name.charAt(i))) {
        i--;
      }
      return ((i > CGLIB_RENAMED_METHOD_PREFIX.length()) &&
            (i < name.length() - 1) && name.charAt(i) == '$');
    }
    return false;
  }
  /**
   * 如果一个方法参数是一个Collection，{@link #getActualTypeOfCollectionElement(Parameter)}函数能够
   * 获得这个参数的泛型类型，如果不是Collection的实现类，或者没有泛型参数，则返回null
   */
  public static Class<?> getActualTypeOfCollectionElement(Parameter param) {
    if(param == null) {
      throw new RuntimeException("Parameter must not be null");
    }
    if(!ClassUtils.isAssignable(param.getType(), Collection.class, false)) {
      return null;
    }
    
    Type type = param.getParameterizedType();
    if (type instanceof ParameterizedType) {
      ParameterizedType elementType = (ParameterizedType) type;
      return (Class<?>) elementType.getActualTypeArguments()[0];
    }
    return null;
  }
  
  /**
   * 返回参数的“真实类型”，所谓“真实类型”，指的是，如果这个参数为Array或者Collection的时候，它的元素的类型。
   * @param parameter 参数
   * @return
   */
  public static Class<?> getParameterActualType(Parameter parameter) {
    Class<?> elementType = null;

    if (ClassUtils.isAssignable(parameter.getType(), Collection.class, false)) {
      elementType = ReflectUtils.getActualTypeOfCollectionElement(parameter);
      if (elementType == null) {
        elementType = String.class;
      }
    } else if (parameter.getType().isArray()) {
      elementType = parameter.getType().getComponentType();
    }

    return elementType;
  }
  
  public static String getParameterName(Parameter parameter, int paramIndex) {
    if (parameter == null) {
      return null;
    }
    // 对于Present的名字，直接返回参数名称
    if (parameter.isNamePresent()) {
      return parameter.getName();
    }
    // 对于用Param标注的参数，返回Param规定的名称
    Param param = parameter.getAnnotation(Param.class);
    if (param != null && StringUtils.isNotBlank(param.value())) {
      return param.value();
    }
    // 对于进没有Present也没有@Param的参数，返回参数类名（驼峰命名）+参数位置索引
    return StringUtils.toCamelCase(parameter.getType().getSimpleName()) + paramIndex;
  }
  
  private static LinkedList<Field> fields(Class<?> clazz) {
    if(clazz == null) {
      throw new RuntimeException("Class must not be null");
    }
    LinkedList<Field> fields = new LinkedList<Field>();
    Field[] fs = getDeclaredFields(clazz);
    if(fs != null) {
      for(Field f : fs) {
        fields.add(f);
      }
    }
    Class<?> searchType = clazz.getSuperclass();
  
    while (searchType != null && Object.class != searchType) {
      fields.addAll(fields(searchType));
      searchType = searchType.getSuperclass();
    }
    
    return fields;
  }

  private static Field[] getDeclaredFields(Class<?> clazz) {
    Field[] result = declaredFieldsCache.get(clazz);
    if (result == null) {
      result = clazz.getDeclaredFields();
      declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
    }
    return result;
  }
  
  private static Method[] getDeclaredMethods(Class<?> clazz) {
    Method[] result = declaredMethodsCache.get(clazz);
    if (result == null) {
      Method[] declaredMethods = clazz.getDeclaredMethods();
      List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
      if (defaultMethods != null) {
        result = new Method[declaredMethods.length + defaultMethods.size()];
        System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
        int index = declaredMethods.length;
        for (Method defaultMethod : defaultMethods) {
          result[index] = defaultMethod;
          index++;
        }
      }
      else {
        result = declaredMethods;
      }
      declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
    }
    return result;
  }

  private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
    List<Method> result = null;
    for (Class<?> ifc : clazz.getInterfaces()) {
      for (Method ifcMethod : ifc.getMethods()) {
        if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
          if (result == null) {
            result = new LinkedList<Method>();
          }
          result.add(ifcMethod);
        }
      }
    }
    return result;
  }
  
  
  
  private ReflectUtils() {

  }
}
