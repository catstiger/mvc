package com.github.catstiger.mvc.converter;

public interface ValueConverter<T> {
  /**
   * 将一个字符串转换为某个对象，实现类应该为单独的类型提供各种实现。
   * @param strValue 给出字符串
   * @return 转换的对象，如果无法转换，返回<code>null</code>
   */
  T stringToObject(String strValue);
  
}
