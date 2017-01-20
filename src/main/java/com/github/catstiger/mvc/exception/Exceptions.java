package com.github.catstiger.mvc.exception;

public abstract class Exceptions {
  /**
   * 返回一个{@link ReadableException}的实例，表明这个异常是人类可读的，可以被渲染到View层。
   * @param message the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
   * @return Instance of {@link ReadableException}
   * @see {@link RuntimeException}
   */
  public static ReadableException readable(String message) {
    return new ReadableException(message);
  }
  
  /**
   * 返回一个{@link ReadableException}的实例，表明这个异常是人类可读的，可以被渲染到View层。
   * @param message the detail message. The detail message is saved for
      later retrieval by the {@link #getMessage()} method.
   * @param cause the cause (which is saved for later retrieval by the
   *         {@link #getCause()} method).  (A <tt>null</tt> value is
   *         permitted, and indicates that the cause is nonexistent or
   *         unknown.)
   * @return Instance of {@link ReadableException}
   * @see {@link RuntimeException}
   */
  public static ReadableException readable(String message, Throwable cause) {
    return new ReadableException(message, cause);
  }
}
