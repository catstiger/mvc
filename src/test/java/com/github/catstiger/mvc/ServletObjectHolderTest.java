package com.github.catstiger.mvc;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ServletObjectHolderTest extends TestCase {
  @Test
  public void testA() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("name", new String[] {"abc"});
    params.put("set", new String[] {"1"});
    params.put("alias", new String[] {"abc", "ABC", "sam"});
    params.put("dept.name", new String[] {"tech"});
    params.put("dept.level", new String[] {"1"});
    params.put("dept.parent.name", new String[] {"LEADER"});
    params.put("dept.parent.level", new String[] {"0"});
    params.put("leader.name", new String[] {"999"});
    params.put("leader.alias", new String[] {"9", "asss", "ppt"});
    params.put("leader.sex", new String[] {"1"});
    
    
    RequestObjectHolder.setRequestParameters(params);
    Map<String, Object> map = RequestObjectHolder.getInheritableParams();
    
    String json = JSON.toJSONString(map, true);
    System.out.println(json);
  }
}
