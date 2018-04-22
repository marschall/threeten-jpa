ThreeTen JPA [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa) [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa)
============

JPA attribute converters for [JSR-310](https://jcp.org/en/jsr/detail?id=310) (Java 8) dates and times.

This project was stop gap measure until [JPA_SPEC-63](https://github.com/javaee/jpa-spec/issues/63) was fixed in JPA 2.2. However JSR-310 support in JPA is [not ready for prime time](https://marschall.github.io/2018/04/22/jpa-jsr-310.html). The project serves the following purposes:

1. provide JSR-310 support for users on JPA 2.1
1. provide support for additional JSR-310 data types not specified by JPA 2.2
1. provide workarounds for driver bugs and missing driver features
1. provide workarounds for JPA implementation bugs

We take inspiration from the JDBC 4.2 and currently support the following conversions:

| ANSI SQL                | Java SE 8         |
| ----------------------- | ----------------- |
| DATE                    | LocalDate         |
| TIME                    | LocalTime         |
| TIMESTAMP               | LocalDateTime     |
| TIMESTAMP WITH TIMEZONE | OffsetTime (*)    |
| TIMESTAMP WITH TIMEZONE | ZonedDateTime (*) |
| INTERVAL YEAR TO MONTH  | Period (*)        |
| INTERVAL DAY TO SECOND  | Duration (*)      |

 (*) requires special extensions, see below

Not supported is converting `TIME WITH TIMEZONE` to `OffsetTime` because it seems [not very useful](https://www.postgresql.org/docs/current/static/datatype-datetime.html#DATATYPE-TIMEZONES).

This project requires Java SE 8 (for the date and time classes) and JPA 2.1 (for the attribute converters). Java SE 9 is supported as well.

This project is very similar to [montanajava/jpaattributeconverters](https://bitbucket.org/montanajava/jpaattributeconverters) or [perceptron8/datetime-jpa](https://github.com/perceptron8/datetime-jpa) and can be used in [much the same way](https://wiki.java.net/blog/montanajava/archive/2014/06/17/using-java-8-datetime-classes-jpa).

Usage
-----

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>threeten-jpa</artifactId>
    <version>1.10.0</version>
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

Converters
----------

You can find a complete list of all converters in the Javadoc

 * [threeten-jpa](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa/1.10.0)
 * [threeten-jpa-jdbc42-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-jdbc42-hibernate/1.10.0)
 * [threeten-jpa-oracle-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-oracle-hibernate/1.10.0)
 * [threeten-jpa-oracle-eclipselink](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-oracle-eclipselink/1.10.0)
 * [threeten-jpa-h2-eclipselink](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-h2-eclipselink/1.10.0)
 * [threeten-jpa-mssql-eclipselink](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-mssql-eclipselink/1.10.0)
 * [threeten-jpa-mssql-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-mssql-hibernate/1.10.0)
 * [threeten-jpa-zoned-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-zoned-hibernate/1.10.0)

Time Zone Support
-----------------

[Time Zone Support](https://github.com/marschall/threeten-jpa/wiki/Time-Zone-Support) is documented on the wiki.

Interval Support
----------------

[Oracle Interval Support](https://github.com/marschall/threeten-jpa/wiki/Oracle-Interval-Support) is documented on the wiki.


Hibernate 5.x
-------------

If you are on Hibernate 5.x and JDBC driver properly supports JDBC 4.2 we strongly [recommend](https://github.com/marschall/threeten-jpa/wiki/JDBC-4.2) using the UserTypes from the `threeten-jpa-jdbc42-hibernate` subproject.

If your driver does not support JDBC 4.2 the hibernate-java8 module introduced in [Hibernate 5.0.0](http://in.relation.to/2015/08/20/hibernate-orm-500-final-release/) (no longer needed for [Hibernate 5.2.0](http://in.relation.to/2016/06/01/hibernate-orm-520-final-release/)) provides functionality that is equivalent to `threeten-jpa` and we recommend you use the Hibernate support instead.

Project Structure
-----------------

The project includes the following submodule:

 * `threeten-jpa` contains portable converters.
 * `threeten-jpa-oracle-eclipselink` contains extensions that work only with Oracle in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`.
 * `threeten-jpa-h2-eclipselink` contains extensions that work only with H2 in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime`.
 * `threeten-jpa-mssql-eclipselink` contains extensions that work only with SQL Server in combination with EclipseLink to map `DATETIMEOFFSET` to `OffsetDateTime`.
 * `threeten-jpa-mssql-hibernate` contains extensions that work only with SQL Server in combination with Hibernate to map `DATETIMEOFFSET` to `OffsetDateTime`.
 * `threeten-jpa-oracle-hibernate` contains extensions that work only with Oracle in combination with Hibernate to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` to `OffsetDateTime`.
 * `threeten-jpa-jdbc42-hibernate` contains extensions that work with any JDBC 4.2 compliant driver in combination with Hibernate. They map
  * `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`
  * `TIMESTAMP [ WITHOUT TIME ZONE ]` to `LocalDateTime` 
  * `TIME` to `LocalTime`
 * `threeten-jpa-oracle-api` contains stub Oracle classes. These are only used for compilation and not present at runtime.
 * `threeten-jpa-oracle-impl` contains the type conversion code from Oracle types to Java 8 types.
 * `threeten-jpa-zoned-hibernate` contains composite user types to support `ZonedDateTime`.


Tested with following JPA providers:
 * EclipseLink
 * Hibernate
 
OpenJPA does currently not yet support JPA 2.1.

Tested with the following databases:
 * Derby
 * H2
 * HSQL
 * MySQL 5.7
 * Oracle 12.1c with the 12.1c driver
 * PostgreS 9.6

License
-------

This project is licensed under the MIT license.

