package com.github.marschall.threeten.jpa.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIMESTAMP_42")
public class JavaTimestamp42 {

  @Id
  @Column(name = "ID")
  private BigInteger id;

  @Column(name = "TIMESTAMP_COLUMN")
  private LocalDateTime localDateTime;

  public BigInteger getId() {
    return this.id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public LocalDateTime getLocalDateTime() {
    return this.localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

}
