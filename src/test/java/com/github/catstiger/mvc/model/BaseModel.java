package com.github.catstiger.mvc.model;

public class BaseModel implements Model<Long>{
  private static final long serialVersionUID = 7758045278995007224L;

  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
