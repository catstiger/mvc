package com.github.catstiger.mvc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ClassUtils;

public final class ValueMapUtils {
  private ValueMapUtils() {
    
  }
  
  
  /**
   * 将一个Request参数Map，根据其Key的特征，转换为一个带有层级结构的Map对象，具体：
   * <p>
   * HttpServletRequest的参数Map通常是这样的：<br>
   * {<br>
   *    "name3" : "Sam",<br>
   *    "name2":  "Lee",<br>
   *    "name1.field1" : "Tech",<br>
   *    "name1.field2" : "f2"<br>
   *    "name1.parent.name" : "Li"<br>
   * }
   * <br>
   * 转换后：<br>
   * {<br>
   *    "name3" : "Sam",<br>
   *    "name2":  "Lee",<br>
   *    "name1": {
   *       "field1" : "Tech",<br>
   *       "field2" : "f2",<br>
   *       "parent": {<br>
   *            "name" : "Li"<br>
   *        }<br>
   *    }
   *    <br>
   * }
   * </p>
   * @param inputFlatMap
   * @param outputCascadeMap
   */
  @SuppressWarnings("unchecked")
  public static void inheritableParams(Map<String, Object> inputFlatMap, Map<String, Object> outputCascadeMap) {
    if(outputCascadeMap == null) {
      return;
    }
    if(inputFlatMap == null || inputFlatMap.isEmpty()) {
      return;
    }
    
    Set<String> keys = inputFlatMap.keySet();
    for(Iterator<String> itr = keys.iterator(); itr.hasNext();) {
      String key = itr.next();
      int dotIndex = key.indexOf(".");
      if(dotIndex > 0) {
        String prefix = key.substring(0, dotIndex);
        Map<String, Object> subParams = null;
        Object item = outputCascadeMap.get(prefix);
        if(item != null && ClassUtils.isAssignable(Map.class, item.getClass())) {
          subParams = (Map<String, Object>) item;
        } else {
          subParams = new HashMap<String, Object>(10);
          outputCascadeMap.put(prefix, subParams);
        }
        subParams.put(key.substring(dotIndex + 1), inputFlatMap.get(key));
      } else {
        Object vo = inputFlatMap.get(key);
        if(vo == null) {
          outputCascadeMap.put(key, null);
        } else if (!vo.getClass().isArray()) {
          outputCascadeMap.put(key, vo);
        } else {
          String[] value = (String[]) vo;
          if(value.length == 0) {
            outputCascadeMap.put(key, null);
          } else if (value.length == 1) {
            outputCascadeMap.put(key, value[0]);
          } else {
            outputCascadeMap.put(key, value);
          }
        }
       
      }
    }
    
    keys = outputCascadeMap.keySet();
    for(Iterator<String> itr = keys.iterator(); itr.hasNext();) {
      String key = itr.next();
      Object val = outputCascadeMap.get(key);
      if(ClassUtils.isAssignable(Map.class, val.getClass())) {
        Map<String, Object> map = new HashMap<String, Object>();
        inheritableParams((Map<String, Object>) val, map);
        outputCascadeMap.put(key, map);
      }
    }
    
  }
  
}
