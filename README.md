ThreeTen JPA [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa) [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa)
============

JPA attribute converters for JSR-310 (Java 8) dates and times.
This is stop gap measure until [JPA_SPEC-63](https://java.net/jira/browse/JPA_SPEC-63) is fixed in JPA 2.2.

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

This project requires Java SE 8 (for the date and time classes) and JPA 2.1 (for the attribute converters).

This project is very similar to [montanajava/jpaattributeconverters](https://bitbucket.org/montanajava/jpaattributeconverters) or [perceptron8/datetime-jpa](https://github.com/perceptron8/datetime-jpa) and can be used in [much the same way](https://wiki.java.net/blog/montanajava/archive/2014/06/17/using-java-8-datetime-classes-jpa).

Usage
-----

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>threeten-jpa</artifactId>
    <version>1.8.0</version>
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

 * [threeten-jpa](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa/1.8.0)
 * [threeten-jpa-jdbc42-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-jdbc42-hibernate/1.8.0)
 * [threeten-jpa-oracle-eclipselink](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-oracle-eclipselink/1.8.0)
 * [threeten-jpa-h2-eclipselink](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-h2-eclipselink/1.8.0)
 * [threeten-jpa-oracle-hibernate](https://www.javadoc.io/doc/com.github.marschall/threeten-jpa-oracle-hibernate/1.8.0)

Time Zone Support
-----------------

[Time Zone Support](https://github.com/marschall/threeten-jpa/wiki/Time-Zone-Support) is documented on the wiki.

Interval Support
----------------

[Oracle-Interval-Support](https://github.com/marschall/threeten-jpa/wiki/Oracle-Interval-Support) is documented on the wiki.

JDBC 4.2
--------

If your driver supports JDBC 4.2 and you use Hibernate we strongly [recommend](https://github.com/marschall/threeten-jpa/wiki/JDBC-4.2) using the UserTypes from the `threeten-jpa-jdbc42-hibernate` subproject instead of the ones from the `threeten-jpa` subproject.

Hibernate 5
-----------

The hibernate-java8 module introduced in [Hibernate 5.0.0](http://in.relation.to/2015/08/20/hibernate-orm-500-final-release/) (no longer needed for [Hibernate 5.2.0](http://in.relation.to/2016/06/01/hibernate-orm-520-final-release/)) provides functionality that is equivalent to `threeten-jpa`. However if your JDBC driver properly supports JDBC 4.2 we strongly [recommend](https://github.com/marschall/threeten-jpa/wiki/JDBC-4.2) using the UserTypes from the `threeten-jpa-jdbc42-hibernate` subproject instead.

Project Structure
-----------------

The project includes the following submodule:

 * `threeten-jpa` contains portable converters.
 * `threeten-jpa-oracle-eclipselink` contains extensions that work only with Oracle in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`.
 * `threeten-jpa-h2-eclipselink` contains extensions that work only with H2 in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime`.
 * `threeten-jpa-oracle-hibernate` contains extensions that work only with Oracle in combination with Hibernate to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` to `OffsetDateTime`.
 * `threeten-jpa-jdbc42-hibernate` contains extensions that work with any JDBC 4.2 compliant driver in combination with Hibernate. They map
  * `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`
  * `TIMESTAMP [ WITHOUT TIME ZONE ]` to `LocalDateTime` 
  * `TIME` to `LocalTime`
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
 * MySQL 5.7
 * Oracle 12.1c with the 12.1c driver
 * PostgreS 9.5

License
-------

This project is licensed under the MIT license.

