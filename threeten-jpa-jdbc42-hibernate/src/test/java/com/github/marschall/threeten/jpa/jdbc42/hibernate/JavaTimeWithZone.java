package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "JAVA_TIME_WITH_ZONE")
public class JavaTimeWithZone {

  @Id
  @Column(name = "ID")
  private BigInteger id;

//  unsupported by jdbc spec
//  @Column(name = "ZONED_TIME")
//  @Type(type = Jdbc42ZonedDateTimeType.NAME)
//  private ZonedDateTime zoned;

  @Column(name = "OFFSET_TIME")
  @Type(type = Jdbc42OffsetDateTimeType.NAME)
  private OffsetDateTime offset;

  @Column(name = "DATE_COLUMN")
  @Type(type = Jdbc42LocalDateType.NAME)
  private LocalDate localDate;

  @Column(name = "TIME_COLUMN")
  @Type(type = Jdbc42LocalTimeType.NAME)
  private LocalTime localTime;

  @Column(name = "TIMESTAMP_COLUMN")
  @Type(type = Jdbc42LocalDateTimeType.NAME)
  private LocalDateTime localDateTime;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

//  public ZonedDateTime getZoned() {
//    return zoned;
//  }
//
//  public void setZoned(ZonedDateTime zoned) {
//    this.zoned = zoned;
//  }

  public OffsetDateTime getOffset() {
    return offset;
  }

  public void setOffset(OffsetDateTime offset) {
    this.offset = offset;
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
