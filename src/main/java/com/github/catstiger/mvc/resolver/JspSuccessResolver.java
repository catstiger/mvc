package com.github.catstiger.mvc.resolver;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.util.ReflectUtils;

/**
 * 如果<br>
 * <ul>
 *     <li>对应的方法的@API标注没有定义resolver</li>
 *     <li>uri后缀采用.htm或者.html</li>
 *     <li>处理URI的过程是成功的，没有出现任何异常。</li>
 * </ul>
 * 则系统会自动调用DefaultJspSuccessResolver，处理JSP重定向等事宜：<br>
 * <ul>
 *     <li>如果结果为一个Collection的子类或者一个数组，则以{@value #ATTR_NAME_COLLECTION}为name保存在request中</li>
 *     <li>如果结果为一个Map,则以Map的key作为names，存储各个Entry</li>
 *     <li>如果结果为一个primitive或者Date,String等对象，则以{@value #ATTR_NAME_PRIMITIVE}为name保存在request中</li>
 *     <li>如果结果为一个Java Bean,则以属性名称保存各个属性</li>
 * </ul>
 * 
 * 然后<br>
 * 根据请求的URI和{@link Initializer#getPageFolder()},重定向到相应的页面
 * @author catstiger
 *
 */
public class JspSuccessResolver extends AbstractResponseResolver {
  
  @SuppressWarnings("rawtypes")
  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object value) {
    String serviceUri = apiResource.getUri();
    String jsp = new StringBuilder(60).append(Initializer.getInstance().getPageFolder()).append(serviceUri)
        .append(Initializer.DEFAULT_TEMPLATE_SUFFIX_JSP).toString();
    
    if(value != null) {
      if(value instanceof Collection || value.getClass().isArray()) {
        request.setAttribute(ATTR_NAME_COLLECTION, value);
      }
      else if (value instanceof Map) {
        Map data = (Map) value;
        for(Iterator itr = data.keySet().iterator(); itr.hasNext();) {
          Object key = itr.next();
          request.setAttribute((String) key, value);
        }
      }
      else if (ConverterFactory.SIMPLE_CONVERTERS.containsKey(value.getClass())) {
        request.setAttribute(ATTR_NAME_PRIMITIVE, value);
      }
      else {
        PropertyDescriptor[] pds = ReflectUtils.getPropertyDescriptors(value.getClass());
        if(pds != null) {
          for(PropertyDescriptor pd : pds) {
            Method readMethod = pd.getReadMethod();  
            if(readMethod != null) {
              Object v = ReflectUtils.invokeMethod(readMethod, value);
              request.setAttribute(pd.getName(), v);
            }
          } 
        }
      }
    }
    
    try {
      request.getRequestDispatcher(jsp).forward(request, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
