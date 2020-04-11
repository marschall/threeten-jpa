package com.github.marschall.threeten.jpa.jdbc42.hibernate.entity;

import java.math.BigInteger;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42LocalTimeType;

@Entity
@Table(name = "JAVA_TIME_42")
public class JavaTime42 {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "TIME_COLUMN")
  @Type(type = Jdbc42LocalTimeType.NAME)
  private LocalTime localTime;

  public BigInteger getId() {
    return this.id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public LocalTime getLocalTime() {
    return this.localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    this.localTime = localTime;
  }

}
