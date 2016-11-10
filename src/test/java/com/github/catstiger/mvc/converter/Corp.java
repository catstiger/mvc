package com.github.catstiger.mvc.converter;

public class Corp {
  private Long id;
  private String corpName;
  private int[] size;
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getCorpName() {
    return corpName;
  }
  public void setCorpName(String corpName) {
    this.corpName = corpName;
  }
  public int[] getSize() {
    return size;
  }
  public void setSize(int[] size) {
    this.size = size;
  }
}
