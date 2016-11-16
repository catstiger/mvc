package com.github.catstiger.mvc.resovler;

import java.util.ArrayList;
import java.util.List;

import com.github.catstiger.mvc.config.ApiResource;

public interface AfterInvoked {
  
  /**
   * URI调用完成之后，调用这个方法，处理后续的工作
   * <ul>
   *     <li>如果调用没有找到ApiResource, 处理404错误</li>
   *     <li>如果调用过程出现异常，处理505错误</li>
   *     <li>如果根据uri判断为JSON请求，则将结果转换为JSON</li>
   *     <li>如果根据uri判断为HTML情况，则重定向到相关页面</li>
   * </ul>
   * @param apiResource 本次调用对应的ApiResource对象
   * @param value 本次调用处理的数据
   */
  public void doAfterInvoked(ApiResource apiResource, Object value);
  
  public static void main(String[]args) {
    List<Long> a = new ArrayList<Long>();
    System.out.println(a.getClass().getSigners());
  }
}
