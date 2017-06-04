package com.github.marschall.threeten.jpa.jdbc42.hibernate;

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

//  ZonedDateTime unsupported by jdbc spec

  @Column(name = "OFFSET_DATE_TIME_COLUMN", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  @Type(type = Jdbc42OffsetDateTimeType.NAME)
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
