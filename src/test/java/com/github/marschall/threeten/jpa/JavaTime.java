package com.github.marschall.threeten.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIME")
public class JavaTime {

  @Column(name = "DATE_COLUMN")
  private LocalDate localDate;
  
  @Column(name = "TIME_COLUMN")
  private LocalTime localTime;
  
  @Column(name = "TIMESTAMP_COLUMN")
  private LocalDateTime localDateTime;

}
