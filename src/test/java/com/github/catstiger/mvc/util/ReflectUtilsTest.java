package com.github.catstiger.mvc.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.Test;

public class ReflectUtilsTest {
  @Test
  public void testGetActualTypeOfCollectionParam() {
    Method[] methods = TestClass.class.getDeclaredMethods();
    for(int i = 0; i < methods.length; i++) {
      Parameter[] params = methods[i].getParameters();
      for(int j = 0; j < params.length; j++) {
        Class<?> clazz = ReflectUtils.getActualTypeOfCollectionElement(params[j]);
        if(clazz != null)
        System.out.println(clazz.getName());
      }
    }
  }
}
