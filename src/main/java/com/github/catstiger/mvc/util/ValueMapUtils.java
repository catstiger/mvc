package com.github.catstiger.mvc.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
  public static Map<String, Object> inheritableParams(Map<String, Object> flatParams) {
    if(flatParams == null || flatParams.isEmpty()) {
      return Collections.emptyMap();
    }
    
    Map<String, Object> params = new HashMap<String, Object>(flatParams.size());
    inheritableParams(flatParams, params);
    
    return params;
  }
  
  @SuppressWarnings("unchecked")
  private static void inheritableParams(Map<String, Object> inputFlatMap, Map<String, Object> outputCascadeMap) {
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
      if(dotIndex > 0) { //带有.的Key
        String prefix = key.substring(0, dotIndex);
        Map<String, Object> subParams = null;
        Object item = outputCascadeMap.get(prefix);
        //如果层级MAP中已经有以prefix为Key的Map，则取出
        if(item != null && ClassUtils.isAssignable(item.getClass(), Map.class, false)) {
          subParams = (Map<String, Object>) item;
        } else { //否则，新建一个空的MAP，并且以prefix为Key保存在层级MAP中
          subParams = new HashMap<String, Object>(10);
          outputCascadeMap.put(prefix, subParams);
        }
        //向子Map中加入一条数据
        subParams.put(key.substring(dotIndex + 1), inputFlatMap.get(key));
      } else { //普通的KEY
        Object vo = inputFlatMap.get(key);
        if(vo == null) {
          outputCascadeMap.put(key, null);
        } else if (!vo.getClass().isArray()) { //如果不是Array则直接存入层级map
          outputCascadeMap.put(key, vo);
        } else {//如果是Array，长度超过1的，以数组存储，否则以数组的第一个元素存储
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
      //如果有子Map，则将子Map转换为层级Map
      if(ClassUtils.isAssignable(val.getClass(), Map.class, false)) {
        Map<String, Object> map = new HashMap<String, Object>();
        inheritableParams((Map<String, Object>) val, map);
        outputCascadeMap.put(key, map);
      }
    }
    
  }
  
}
