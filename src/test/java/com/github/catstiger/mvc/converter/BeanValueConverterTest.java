package com.github.catstiger.mvc.converter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.util.ValueMapUtils;

import junit.framework.TestCase;

public class BeanValueConverterTest extends TestCase {
  
  @Test
  public void testConvert() {
    Map<String, Object> map = new HashMap<String, Object>();
    
    map.put("id", "997674");
    map.put("firstName", "Sam");
    map.put("lastName", "Lee");
    map.put("isActive", "true");
    map.put("birth", "2015-09-18");
    map.put("score", "99809.2984");
    map.put("props", new String[]{"998", "Leuu", "Ying", "Hong"});
    map.put("dept.id", "88567");
    map.put("dept.deptName", "Computer");
    map.put("dept.list", new String[]{"998", "7789", "45634", "3452"});
    map.put("dept.corp.corpName", "V1");
    map.put("dept.corp.id", "985873");
    map.put("dept.corp.size", new String[]{"998", "12", "33", "44"});
    
    Map<String, Object> cascaded = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(map, cascaded);
    
    System.out.println(JSON.toJSONString(cascaded, true));
    
    BeanValueConverter bvc = new BeanValueConverter(Employee.class);
    Employee emp = (Employee) bvc.convert(cascaded);
    System.out.println(JSON.toJSONString(emp, true));
    
    long t1 = new Date().getTime();
    for(int i = 0; i < 10000; i++) {
      bvc.convert(cascaded);
    }
    long t2 = new Date().getTime();
    System.out.println((t2 - t1)/1000);
  }
}
