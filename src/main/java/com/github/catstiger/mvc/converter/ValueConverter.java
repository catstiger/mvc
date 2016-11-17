package com.github.catstiger.mvc.converter;

/**
 * 转换器，将某个对象（在Web环境下是一个String或者Map，或者String数组），转换为其他对象。
 * 用于将客户端提交的参数，转换为系统需要的数据格式。实现类必须是线程安全的。 另外，实现类必须
 * 提供缺省构造方法(none-arguments constructor)。
 * @author catstiger
 * @param <T>
 */
public interface ValueConverter<T> {
  /**
   * 将一个字符串转换为某个对象，实现类应该为单独的类型提供各种实现。
   * @param value 给出被转换的数据,对于Java Bean，是一个Map<String, Object>对象，对于Array，List，Set等，是一个数组，
   * 对于primitive类型的数据，是一个代表此数据的String。
   * @return 转换的对象，如果无法转换，返回<code>null</code>
   */
  T convert(Object value);
  
  /**
   * This marker class is only to be used with annotations, to
   * indicate that <b>no converter is to be used</b>.
   */
  public abstract static class None
  implements ValueConverter<Object> { }
  
}
