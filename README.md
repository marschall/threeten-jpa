ThreeTen JPA [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-jpa)
============

JPA attribute converters for JSR-310 (Java 8) dates and times.
This is stop gap measure until [JPA_SPEC-63](https://java.net/jira/browse/JPA_SPEC-63) is fixed in JPA 2.2 or [HHH-8844](https://hibernate.atlassian.net/browse/HHH-8844) in fixed in Hibernate 5.

We take inspiration from the JDBC 4.2 and currently supports the following conversions:

| ANSI SQL   | Java SE 8      |
| ---------- | -------------- |
| DATE       | LocalDate      |
| TIME       | LocalTime      |
| TIMESTAMP  | LocalDateTime  |
| TIMESTAMP  | Instant        |

Not supported is converting `TIME WITH TIMEZONE` to `OffsetTime` because there is no way of accessing this from JDBC/JPA.
Converting `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime` is only supported with the Oracle EclipseLink extension.

This project requires Java SE 8 (for the date and time classes) and JPA 2.1 (for the attribute converters).

This project is very similar to [montanajava/jpaattributeconverters](https://bitbucket.org/montanajava/jpaattributeconverters) or [perceptron8/datetime-jpa](https://github.com/perceptron8/datetime-jpa) and can be used in [much the same way](https://weblogs.java.net/blog/montanajava/archive/2014/06/17/using-java-8-datetime-classes-jpa).

Usage
-----

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>threeten-jpa</artifactId>
    <version>1.2.1</version>
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

All the converters have set `Converter#autoApply()` to `true` to they're automatically applied to all entities in the same persistence unit.

The additional Oracle EclipseLink features require and additional dependency.

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>threeten-jpa-oracle-eclipselink</artifactId>
    <version>1.1.0</version>
</dependency>
```

Note the Oracle driver module has to be visible to the deployment (eg [Class Loading in WildFly](https://docs.jboss.org/author/display/WFLY9/Class+Loading+in+WildFly)).

Time Zone Support
-----------------

Databases that support `TIMESTAMP WITH TIME ZONE`:

 * Oracle
 * HSQL
 * PostgreSQL
 * SQL Server

Unfortunately both the HSQL and PostgreSQL have either incomplete or buggy implementation of JDBC 4.2 and therefore can't be supported.

|             | Oracle                          | SQL Server                    |
| ----------- | ------------------------------- | ----------------------------- |
| EclipseLink | threeten-jpa-oracle-eclipselink | :x:                           |
| Hibernate   | threeten-jpa-oracle-hibernate   | threeten-jpa-jdbc42-hibernate |

SQL Server support is theoretical and has not been tested.

Databases that do *not* support `TIMESTAMP WITH TIME ZONE`:

 * DB2
 * Derby
 * Firebird
 * H2
 * MySQL

Project Structure
-----------------
The `threeten-jpa` submodule includes portable converters for the conversions above.

The `threeten-jpa-oracle-eclipselink` includes extensions that work only with Oracle in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `ZonedDateTime` or `OffsetDateTime`.

The `threeten-jpa-oracle-hibernate` includes extensions that work only with Oracle in combination with Hibernate to map `TIMESTAMP WITH TIMEZONE` `ZonedDateTime` to `OffsetDateTime`.

The `threeten-jpa-jdbc42-hibernate` includes extensions that work with any JDBC 4.2 compliant driver in combination with Hibernate to map to map `TIMESTAMP WITH TIMEZONE` `ZonedDateTime` to `OffsetDateTime`.

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
