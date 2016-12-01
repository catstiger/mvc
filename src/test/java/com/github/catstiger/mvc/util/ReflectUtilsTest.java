package com.github.catstiger.mvc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.Test;

import com.github.catstiger.mvc.model.Student;

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
  
  @Test
  public void testAct() {
    Field[] fs = ReflectUtils.getFields(Student.class);
    for(int i = 0; i < fs.length; i++) {
      System.out.println(fs[i].getName());
    }
  }
}
