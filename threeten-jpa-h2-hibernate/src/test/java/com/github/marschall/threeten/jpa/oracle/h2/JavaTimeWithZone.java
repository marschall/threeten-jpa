package com.github.marschall.threeten.jpa.oracle.h2;

import java.math.BigInteger;
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

  @Column(name = "TIMESTAMP_WITH_TIMEZONE_COLUMN")
  @Type(type = H2OffsetDateTimeType.NAME)
  private OffsetDateTime offset;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public OffsetDateTime getOffset() {
    return offset;
  }

  public void setOffset(OffsetDateTime offset) {
    this.offset = offset;
  }

}
