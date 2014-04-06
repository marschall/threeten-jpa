package com.github.marschall.threeten.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "JAVA_TIME")
public class JavaTime {

  private LocalDate localDate;
  
  private LocalTime localTime;
  
  private LocalDateTime localDateTime;

}
