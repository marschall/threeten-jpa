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

Not supported are TIME WITH TIMEZONE (to OffsetTime) and TIMESTAMP WITH TIMEZONE (to OffsetDateTime) because there's no portable way to access them from JDBC (eg [TIMESTAMPTZ](http://docs.oracle.com/cd/E11882_01/appdev.112/e13995/oracle/sql/TIMESTAMPTZ.html)).

The project requires Java SE 8 and JPA 2.1 which introduces attribute converters.

Tested with following JPA providers:
 * EclipseLink
 * Hibernate
 
OpenJPA does currently not yet support JPA 2.1.

Tested with the following databases:
 * Derby
 * H2
 * HSQL
 * Oracle 11.2g with with 12.1c driver
 * PostgreS 9.3