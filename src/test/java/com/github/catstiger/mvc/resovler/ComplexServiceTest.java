package com.github.catstiger.mvc.resovler;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.AbstractTestCase;
import com.github.catstiger.mvc.ServletObjectHolderMock;
import com.github.catstiger.mvc.SpringHelper;
import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.model.Student;
import com.github.catstiger.mvc.service.ServiceInvoker;
import com.github.catstiger.mvc.util.ValueMapUtils;

public class ComplexServiceTest extends AbstractTestCase {
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    System.out.println("Init application context...");
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-test.xml");
    SpringHelper.initApplicationContext(ctx);  
    ServletObjectHolderMock.init(); //Mock
    Initializer.getInstance().loadApiResources("com.github.catstiger");
  }
  
  @Test
  public void testSave() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("age", new String[]{"20"});
    map.put("id", new String[]{"859684"});
    map.put("birth", new String[] {"1996-09-08"});
    
    Map<String, Object> params = ValueMapUtils.inheritableParams(map);
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/complex_service/save");
    Student st = (Student) ServiceInvoker.invoke(api, params);
    assertNotNull(JSON.toJSONString(st));
  }
}
