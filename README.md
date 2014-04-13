ThreeTen JPA [![Build Status](https://travis-ci.org/marschall/threeten-jpa.svg?branch=master)](https://travis-ci.org/marschall/threeten-jpa)
============

JPA attribute converters for JSR-310 dates and times.
This is stop gap measure until [JPA_SPEC-63](https://java.net/jira/browse/JPA_SPEC-63) is fixed in JPA 2.2.

| ANSI SQL   | Java SE 8      |
| ---------- | -------------- |
| DATE       | LocalDate      |
| TIME       | LocalTime      |
| TIMESTAMP  | LocalDateTime  |

Not supported are TIME WITH TIMEZONE (to OffsetTime) and TIMESTAMP WITH TIMEZONE (to OffsetDateTime) because there's no portable way to access them from JDBC.

The project requires Java SE 8 obviously and JPA 2.1 which introduces attribute converters.

Tested with following JPA providers:
 * EclipseLink
 * Hibernate

Tested with the following databases:
 * Derby
 * H2
 * HSQL