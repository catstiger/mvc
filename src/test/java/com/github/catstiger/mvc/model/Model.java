package com.github.catstiger.mvc.model;

import java.io.Serializable;

public interface Model<T> extends Serializable {
  
  public abstract T getId();
  
  public abstract void setId(T id);
}