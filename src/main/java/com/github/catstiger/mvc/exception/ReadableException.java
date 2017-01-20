package com.github.catstiger.mvc.exception;

/**
 * 对于可以预见的异常，要以明确的，可读的信息抛出。此时，MVC框架不会打印堆栈信息，直接向View层Render异常信息
 * @author catstiger
 *
 */
public class ReadableException extends RuntimeException {

  public ReadableException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReadableException(String message) {
    super(message);
  }

}
