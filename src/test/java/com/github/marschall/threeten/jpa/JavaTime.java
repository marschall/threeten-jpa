package com.github.marschall.threeten.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "JAVA_TIME")
public class JavaTime {

  @Column(name = "DATE_COLUMN")
  //  @Temporal(TemporalType.DATE)
  private LocalDate localDate;

  @Column(name = "TIME_COLUMN")
  //  @Temporal(TemporalType.TIME)
  private LocalTime localTime;

  @Column(name = "TIMESTAMP_COLUMN")
  //  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime localDateTime;

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
