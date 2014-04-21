ThreeTen JPA [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa)
============

JPA attribute converters for JSR-310 dates and times.
This is stop gap measure until [JPA_SPEC-63](https://java.net/jira/browse/JPA_SPEC-63) is fixed in JPA 2.2.

Takes inspiration from the JDBC 4.2 and currently supports the following conversions:

| ANSI SQL   | Java SE 8      |
| ---------- | -------------- |
| DATE       | LocalDate      |
| TIME       | LocalTime      |
| TIMESTAMP  | LocalDateTime  |

Not supported is converting `TIME WITH TIMEZONE` to `OffsetTime` because there is no way of accessing this from JDBC/JPA.
Converting `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime` is only supported with the Oracle EclipseLink extension.

The project requires Java SE 8 and JPA 2.1.

Project Structure
-----------------
The `threeten-jpa` submodule includes portable converters for the conversions above.

The `threeten-jpa-oracle-eclipselink` includes extensions that work only with Oracle in combination with EclipseLink to map `TIMESTAMP WITH TIMEZONE` to `OffsetDateTime`. Only EclipseLink supports [TIMESTAMPTZ](http://docs.oracle.com/cd/E11882_01/appdev.112/e13995/oracle/sql/TIMESTAMPTZ.html).

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