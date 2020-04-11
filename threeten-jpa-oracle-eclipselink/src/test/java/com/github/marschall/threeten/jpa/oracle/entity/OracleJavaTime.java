package com.github.marschall.threeten.jpa.oracle.entity;

import java.math.BigInteger;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_DATE_TIME_ORACLE_ECLIPSELINK")
public class OracleJavaTime {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "OFFSET_COLUMN")
  private OffsetDateTime offsetDateTime;

  @Column(name = "ZONE_COLUMN")
  private ZonedDateTime zonedDateTime;

//  @Temporal(TemporalType.TIMESTAMP)
//  @Column(name = "CALENDAR_COLUMN")
//  private Calendar calendar;

  @Column(name = "YEAR_TO_MONTH_COLUMN")
  private Period period;

  @Column(name = "DAY_TO_SECOND_COLUMN")
  private Duration duration;

  public BigInteger getId() {
    return this.id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public OffsetDateTime getOffsetDateTime() {
    return this.offsetDateTime;
  }

  public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
    this.offsetDateTime = offsetDateTime;
  }

  public ZonedDateTime getZonedDateTime() {
    return this.zonedDateTime;
  }

  public void setZonedDateTime(ZonedDateTime zonedDateTime) {
    this.zonedDateTime = zonedDateTime;
  }
//
//  public Calendar getCalendar() {
//    return this.calendar;
//  }
//
//  public void setCalendar(Calendar calendar) {
//    this.calendar = calendar;
//  }

  public Period getPeriod() {
    return this.period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  public Duration getDuration() {
    return this.duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

}
