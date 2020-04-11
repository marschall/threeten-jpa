package com.github.marschall.threeten.jpa.zoned.hibernate.entity;

import java.math.BigInteger;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.github.marschall.threeten.jpa.zoned.hibernate.ZonedDateTimeType;

@Entity
@Table(name = "JAVA_TIMESTAMP_42_ZONED")
public class JavaTimestamp42Zoned {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Type(type = ZonedDateTimeType.NAME)
  @Columns(columns = {
      @Column(name = "TIMESTAMP_UTC"),
      @Column(name = "ZONE_ID")
  })
  private ZonedDateTime zonedDateTime;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public ZonedDateTime getZonedDateTime() {
    return zonedDateTime;
  }

  public void setZonedDateTime(ZonedDateTime zonedDateTime) {
    this.zonedDateTime = zonedDateTime;
  }

}
