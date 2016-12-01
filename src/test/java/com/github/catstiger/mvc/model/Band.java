package com.github.catstiger.mvc.model;

public class Band extends BaseModel {
  private String name;
  private String descn;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescn() {
    return descn;
  }

  public void setDescn(String descn) {
    this.descn = descn;
  }
}
