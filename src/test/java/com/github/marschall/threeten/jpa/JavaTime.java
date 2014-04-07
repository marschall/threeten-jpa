package com.github.marschall.threeten.jpa;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIME")
public class JavaTime {
  
  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "DATE_COLUMN")
////  @Temporal(TemporalType.DATE)
  @Convert(converter = LocalDateConverter.class) // removing this breaks Hibernate on Derby
  private LocalDate localDate;
//
  @Column(name = "TIME_COLUMN")
//  //  @Temporal(TemporalType.TIME)
  @Convert(converter = LocalTimeConverter.class) // removing this breaks Hibernate on Derby
  private LocalTime localTime;
//
  @Column(name = "TIMESTAMP_COLUMN")
//  //  @Temporal(TemporalType.TIMESTAMP)
  @Convert(converter = LocalDateTimeConverter.class) // removing this breaks Hibernate on Derby
  private LocalDateTime localDateTime;
//  private Timestamp localDateTime;

//  public LocalDate getLocalDate() {
//    return localDate;
//  }
//
//  public void setLocalDate(LocalDate localDate) {
//    this.localDate = localDate;
//  }
//
//  public LocalTime getLocalTime() {
//    return localTime;
//  }
//
//  public void setLocalTime(LocalTime localTime) {
//    this.localTime = localTime;
//  }
//
//  public LocalDateTime getLocalDateTime() {
//    return localDateTime;
//  }
//
//  public void setLocalDateTime(LocalDateTime localDateTime) {
//    this.localDateTime = localDateTime;
//  }

}
