package com.github.marschall.threeten.jpa;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIME_42")
public class JavaTime {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "DATE_COLUMN")
  private LocalDate localDate;

  @Column(name = "TIME_COLUMN")
  private LocalTime localTime;

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

  public LocalTime getLocalTime() {
    return localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    this.localTime = localTime;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

}
