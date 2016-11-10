package com.github.catstiger.mvc.converter;

import java.util.List;

import com.github.catstiger.mvc.annotation.Param;

public class Department {
  private String deptName;
  private Long id;
  private Corp corp;
  private List<Long> list;
  
  public String getDeptName() {
    return deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Corp getCorp() {
    return corp;
  }
  public void setCorp(Corp corp) {
    this.corp = corp;
  }
  public List<Long> getList() {
    return list;
  }
  @Param(elementType = Long.class)
  public void setList(List<Long> list) {
    this.list = list;
  }

}
