package com.github.catstiger.mvc.model;

public class BaseModel implements Model<Long>{
  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
