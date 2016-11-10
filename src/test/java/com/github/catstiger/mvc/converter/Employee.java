package com.github.catstiger.mvc.converter;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Employee {
  private String firstName;
  private String lastName;
  private Boolean isActive;
  private Date birth;
  private Integer relayTimes;
  private Long id;
  private Double score;
  private String[] props;
  private Department dept;
  
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public Boolean getIsActive() {
    return isActive;
  }
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }
  @JSONField(format = "yyyy-MM-dd")
  public Date getBirth() {
    return birth;
  }
  public void setBirth(Date birth) {
    this.birth = birth;
  }
  public Integer getRelayTimes() {
    return relayTimes;
  }
  public void setRelayTimes(Integer relayTimes) {
    this.relayTimes = relayTimes;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Double getScore() {
    return score;
  }
  public void setScore(Double score) {
    this.score = score;
  }
  public String[] getProps() {
    return props;
  }
  public void setProps(String[] props) {
    this.props = props;
  }
  public Department getDept() {
    return dept;
  }
  public void setDept(Department dept) {
    this.dept = dept;
  }
}
