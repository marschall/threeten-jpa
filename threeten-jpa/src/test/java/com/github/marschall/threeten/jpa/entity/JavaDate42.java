package com.github.marschall.threeten.jpa.entity;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_DATE_42")
public class JavaDate42 {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "DATE_COLUMN")
  private LocalDate localDate;

  public BigInteger getId() {
    return this.id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public LocalDate getLocalDate() {
    return this.localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

}
