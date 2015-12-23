ThreeTen JPA [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa)
============

JPA attribute converters for JSR-310 (Java 8) dates and times.
This is stop gap measure until [JPA_SPEC-63](https://java.net/jira/browse/JPA_SPEC-63) is fixed in JPA 2.2.

We take inspiration from the JDBC 4.2 and currently support the following conversions:

| ANSI SQL                | Java SE 8         |
| ----------------------- | ----------------- |
| DATE                    | LocalDate         |
| TIME                    | LocalTime         |
| TIMESTAMP               | LocalDateTime     |
| TIMESTAMP               | Instant           |
| TIMESTAMP WITH TIMEZONE | OffsetTime (*)    |
| TIMESTAMP WITH TIMEZONE | ZonedDateTime (*) |

 (*) Converting `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime` or `ZonedDateTime` requires special extensions.

Not supported is converting `TIME WITH TIMEZONE` to `OffsetTime` because it seems [not very useful](http://www.postgresql.org/docs/9.4/static/datatype-datetime.html#DATATYPE-TIMEZONES).

This project requires Java SE 8 (for the date and time classes) and JPA 2.1 (for the attribute converters).

This project is very similar to [montanajava/jpaattributeconverters](https://bitbucket.org/montanajava/jpaattributeconverters) or [perceptron8/datetime-jpa](https://github.com/perceptron8/datetime-jpa) and can be used in [much the same way](https://wiki.java.net/blog/montanajava/archive/2014/06/17/using-java-8-datetime-classes-jpa).

Usage
-----

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>threeten-jpa</artifactId>
    <version>1.3.0</version>
</dependency>
```

```xml
<persistence-unit>
    …
    <class>com.github.marschall.threeten.jpa.LocalTimeConverter</class>
    <class>com.github.marschall.threeten.jpa.LocalDateConverter</class>
    <class>com.github.marschall.threeten.jpa.LocalDateTimeConverter</class>
    …
</persistence-unit>
```

```java
@Entity
public class SampleEntity {

  @Column
  private LocalDate localDate;

  @Column
  private LocalTime localTime;

  @Column
  private LocalDateTime localDateTime;
  
}
```

All the converters have set `Converter#autoApply()` to `true` so they're automatically applied to all entities in the same persistence unit.

Time Zone Support
-----------------

Supporting time zones needs both database and driver support.

Databases that support `TIMESTAMP WITH TIME ZONE`:

 * Oracle
 * HSQL
 * PostgreSQL
 * SQL Server

Unfortunately both HSQL and PostgreSQL have either incomplete or buggy implementations of JDBC 4.2 and therefore can't be supported. In addition PostgreSQL throws the time zone away when storing. To make matters more complicated Oracle does not yet have a JDBC 4.2 driver and supports accessing time zones only through [proprietary APIs](http://docs.oracle.com/cd/E11882_01/appdev.112/e13995/oracle/sql/TIMESTAMPTZ.html).

Databases that do *not* support `TIMESTAMP WITH TIME ZONE`:

 * DB2
 * Derby
 * Firebird
 * H2
 * MySQL

This results in the following support matrix.

|             | Oracle                          | SQL Server                    |
| ----------- | ------------------------------- | ----------------------------- |
| EclipseLink | threeten-jpa-oracle-eclipselink | :x:                           |
| Hibernate   | threeten-jpa-oracle-hibernate   | threeten-jpa-jdbc42-hibernate |

SQL Server support is untested.

`threeten-jpa-oracle-eclipselink` contains JPA attribute converters which need to be listed in `persistence.xml`

```xml
<persistence-unit>
    …
    <class>com.github.marschall.threeten.jpa.oracle.OracleOffsetDateTimeConverter</class>
    <class>com.github.marschall.threeten.jpa.oracle.OracleZonedDateTimeConverter</class>
    …
</persistence-unit>
```

`threeten-jpa-oracle-hibernate` contains Hibernate user types which need to be used using `@Type`:

```java
@Entity
public class SampleEntity {

  @Column
  @Type(type = OracleZonedDateTimeType.NAME)
  private ZonedDateTime onedDateTime;

  @Column
  @Type(type = OracleOffsetDateTimeType.NAME)
  private OffsetDateTime offsetDateTime;

}
```


`threeten-jpa-jdbc42-hibernate` contains Hibernate user types which need to be used using `@Type`:

```java
@Entity
public class SampleEntity {

  @Column
  @Type(type = Jdbc42ZonedDateTimeType.NAME)
  private ZonedDateTime onedDateTime;

  @Column
  @Type(type = Jdbc42OffsetDateTimeType.NAME)
  private OffsetDateTime offsetDateTime;

}
```

Note the Oracle driver module has to be visible to the deployment (eg [Class Loading in WildFly](https://docs.jboss.org/author/display/WFLY9/Class+Loading+in+WildFly)).

Project Structure
-----------------

The project includes the following submodule:

 * `threeten-jpa` contains portable converters for the conversions above.
 * `threeten-jpa-oracle-eclipselink` contains extensions that work only with Oracle in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`.
 * `threeten-jpa-oracle-hibernate` contains extensions that work only with Oracle in combination with Hibernate to map `TIMESTAMP WITH TIMEZONE` `ZonedDateTime` to `OffsetDateTime`.
 * `threeten-jpa-jdbc42-hibernate` contains extensions that work with any JDBC 4.2 compliant driver in combination with Hibernate to map to map `TIMESTAMP WITH TIMEZONE` `ZonedDateTime` to `OffsetDateTime`.
 * `threeten-jpa-oracle-api` contains stub Oracle classes. These are only used for compilation and not present at runtime.
 * `threeten-jpa-oracle-impl` contains the type conversion code from Oracle types to Java 8 types.


Tested with following JPA providers:
 * EclipseLink
 * Hibernate
 
OpenJPA does currently not yet support JPA 2.1.

Tested with the following databases:
 * Derby
 * H2
 * HSQL
 * Oracle 11.2g with the 12.1c driver
 * PostgreS 9.3
