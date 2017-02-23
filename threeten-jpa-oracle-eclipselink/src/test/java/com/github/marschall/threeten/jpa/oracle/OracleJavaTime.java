package com.github.marschall.threeten.jpa.oracle;

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
@Table(name = "JAVA_TIME")
public class OracleJavaTime {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "OFFSET_COLUMN")
  private OffsetDateTime offsetDateTime;

  @Column(name = "ZONE_COLUMN")
  private ZonedDateTime zonedDateTime;

  @Column(name = "YEAR_TO_MONTH_COLUMN")
  private Period period;

  @Column(name = "DAY_TO_SECOND_COLUMN")
  private Duration duration;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public OffsetDateTime getOffsetDateTime() {
    return offsetDateTime;
  }

  public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
    this.offsetDateTime = offsetDateTime;
  }

  public ZonedDateTime getZonedDateTime() {
    return zonedDateTime;
  }

  public void setZonedDateTime(ZonedDateTime zonedDateTime) {
    this.zonedDateTime = zonedDateTime;
  }

  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

}
