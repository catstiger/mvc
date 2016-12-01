package com.github.catstiger.mvc.model;

import java.util.Date;

public class Student extends BaseModel {
  private String name;
  private String sex;
  private Date birth;
  private Integer level;
  private Band band;

  public Band getBand() {
    return band;
  }

  public void setBand(Band band) {
    this.band = band;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public Date getBirth() {
    return birth;
  }

  public void setBirth(Date birth) {
    this.birth = birth;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }
}
