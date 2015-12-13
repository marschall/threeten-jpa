package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.math.BigInteger;
import java.time.OffsetDateTime;

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

  @Column(name = "TIMESTAMP_WITH_TIMEZONE_COLUMN")
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

  public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
    this.offsetDateTime = offsetDateTime;
  }

}
