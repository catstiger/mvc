package com.github.catstiger.mvc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.resovler.ServiceInvoker;

import junit.framework.TestCase;

public abstract class AbstractTestCase extends TestCase {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected Method getMethodByName(Class<?> clazz, String methodName) {
    Method[] methods = clazz.getMethods();
    for (int i = 0; i < methods.length; i++) {
      if (methods[i].getName().equals(methodName)) {
        return methods[i];
      }
    }
    return null;
  }

  /**
   * 根据Bean类型，生成测试数据，测试数据的格式符合HttpServletRequest#getParameterMap()返回的数据格式
   * 
   * @param beanClass
   *          给出Bean类型
   * @param justPrimitive
   *          是否只处理primitive类型，如果为false,则递归生成包含的bean的测试数据
   * @return
   */
  protected Map<String, Object> prepareTestData(Class<?> beanClass, boolean justPrimitive) {
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(beanClass);
    Map<String, Object> data = new HashMap<String, Object>(pds.length);
    for (PropertyDescriptor pd : pds) {
      if (pd.getName().equals("class")) {
        continue;
      }
      Object value = null;
      String name = pd.getName();
      Class<?> propType = pd.getPropertyType();
      
      if (propType.isArray()) {
        value = getSingleArray(propType, justPrimitive);
        data.put(name, value);
      } else if (ConverterFactory.SIMPLE_CONVERTERS.containsKey(propType) || propType.isPrimitive()) {
        value = new String[] { getSingleString(propType, justPrimitive) };
        data.put(name, value);
      } else if (!justPrimitive) {
        logger.debug("Inherity call {}", propType);
        Map<String, Object> childData = prepareTestData(propType, justPrimitive);
        Set<String> keys = childData.keySet();
        for (String key : keys) {
          data.put(name + "." + key, childData.get(key));
        }
      } else {
        value = new String[] { "null" };
        data.put(name, value);
      }
    }

    return data;
  }

  protected Map<String, Object> prepareTestData(Method method, boolean justPrimitive) {
    Parameter[] params = method.getParameters();
    Map<String, Object> data = new HashMap<String, Object>(params.length);

    if (params.length == 1) {
      logger.debug("Generic single parameter value.");
      Class<?> paramType = params[0].getType();
      String paramName = ServiceInvoker.getParameterName(params[0], 0);
      
      if(ConverterFactory.SIMPLE_CONVERTERS.containsKey(paramType) || paramType.isPrimitive()) {
        data.put(paramName, new String[]{getSingleString(paramType, justPrimitive)});
      } else if (paramType.isArray()) {
        Object value = getSingleArray(paramType, justPrimitive);
        data.put(paramName, value);
      } else {
        Map<String, Object> value = this.prepareTestData(paramType, justPrimitive);
        data.putAll(value);
      }
      
    } else {
      
      for (int i = 0; i < params.length; i++) {
        Class<?> paramType = params[i].getType();
        String paramName = ServiceInvoker.getParameterName(params[i], i);
        if (paramType.isArray()) {
          Object value = getSingleArray(paramType, justPrimitive);
          data.put(paramName, value);
        } else if (ConverterFactory.SIMPLE_CONVERTERS.containsKey(paramType) || paramType.isPrimitive()) {
          Object value = new String[] { getSingleString(paramType, justPrimitive) };
          data.put(paramName, value);
        } else {
          Map<String, Object> paramData = this.prepareTestData(paramType, justPrimitive);
          Set<String> keys = paramData.keySet();
          for (String key : keys) {
            data.put(paramName + "." + key, paramData.get(key));
          }
        }
      }
    }

    return data;
  }
  
  protected String[] getSingleArray(Class<?> targetClass, boolean justPrimitive) {
    String[] value = null;
    if ((targetClass.isArray() && targetClass.getComponentType() != null)) {
      Object strArr = Array.newInstance(String.class, 10);
      for (int i = 0; i < 10; i++) {
        String ele = getSingleString(targetClass.getComponentType(), justPrimitive);
        Array.set(strArr, i, (String) ele);
      }
      value = (String[]) strArr;
    }
    
    return value;
  }

  protected String getSingleString(Class<?> targetClass, boolean justPrimitive) {
    String value;
    if (targetClass == int.class || targetClass == Integer.class || targetClass == BigInteger.class) {
      value = RandomStringUtils.randomNumeric(6);
    } else if (targetClass == long.class || targetClass == Long.class) {
      value = RandomStringUtils.randomNumeric(8);
    } else if (targetClass == double.class || targetClass == Double.class || targetClass == BigDecimal.class) {
      value = String.valueOf(new Random().nextDouble());
    } else if (targetClass == float.class || targetClass == Float.class) {
      value = String.valueOf(new Random().nextFloat());
    } else if (targetClass == String.class) {
      value = "S_" + RandomStringUtils.randomNumeric(10);
    } else if (targetClass == boolean.class || targetClass == Boolean.class) {
      value = String.valueOf(new Random().nextBoolean());
    } else if (targetClass == Date.class) {
      value = new DateTime(new Date()).toString(Initializer.DEFAULT_DATE_FORMAT);
    } else if ((targetClass.isArray() && targetClass.getComponentType() != null)) {
      Object strArr = Array.newInstance(String.class, 10);
      for (int i = 0; i < 10; i++) {
        String ele = getSingleString(targetClass.getComponentType(), justPrimitive);
        Array.set(strArr, i, (String) ele);
      }
      value = JSON.toJSONString(strArr);
    } else {
      value = "null";
    }

    return value;
  }

}
