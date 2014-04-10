ThreeTen JPA
============

JPA attribute converters for JSR-310 dates and times.

| ANSI SQL   | Java SE 8      |
| ---------- | -------------- |
| DATE       | LocalDate      |
| TIME       | LocalTime      |
| TIMESTAMP  | LocalDateTime  |

The project requires Java SE 8 obviously and JPA 2.1 which introduces attribute converters.

Not supported are TIME WITH TIMEZONE (to OffsetTime) and TIMESTAMP WITH TIMEZONE (to OffsetDateTime) because there's no portable way to access them from JDBC.

Tested with following JPA providers:
 * EclipseLink
 * Hibernate

Tested with the following databases:
 * Derby
 * H2