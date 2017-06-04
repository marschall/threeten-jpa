package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.math.BigInteger;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

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

  @Column(name = "ZONED_TIME")
  @Type(type = OracleZonedDateTimeType.NAME)
  private ZonedDateTime zoned;

  @Column(name = "OFFSET_DATE_TIME_COLUMN")
  @Type(type = OracleOffsetDateTimeType.NAME)
  private OffsetDateTime offset;

  @Column(name = "YEAR_TO_MONTH_COLUMN")
  @Type(type = OraclePeriodType.NAME)
  private Period period;

  @Column(name = "DAY_TO_SECOND_COLUMN")
  @Type(type = OracleDurationType.NAME)
  private Duration duration;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public ZonedDateTime getZoned() {
    return zoned;
  }

  public void setZoned(ZonedDateTime zoned) {
    this.zoned = zoned;
  }

  public OffsetDateTime getOffset() {
    return offset;
  }

  public void setOffset(OffsetDateTime offset) {
    this.offset = offset;
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
