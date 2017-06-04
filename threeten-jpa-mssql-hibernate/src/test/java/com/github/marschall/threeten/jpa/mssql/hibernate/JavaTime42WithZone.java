package com.github.marschall.threeten.jpa.mssql.hibernate;

import java.math.BigInteger;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "JAVA_TIME_42_WITH_ZONE")
public class JavaTime42WithZone {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "OFFSET_DATE_TIME_COLUMN")
  @Type(type = MssqlOffsetDateTimeType.NAME)
  private OffsetDateTime offsetDateTime;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public OffsetDateTime getOffsetDateTime() {
    return offsetDateTime;
  }

  public void setOffsetDateTime(OffsetDateTime offset) {
    this.offsetDateTime = offset;
  }

}
