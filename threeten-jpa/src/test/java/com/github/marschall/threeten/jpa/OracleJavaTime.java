package com.github.marschall.threeten.jpa;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIME")
public class OracleJavaTime {
  // Oracle has no TIME data type

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "DATE_COLUMN")
  private LocalDate localDate;

  @Column(name = "TIMESTAMP_COLUMN")
  private LocalDateTime localDateTime;
  

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

}
