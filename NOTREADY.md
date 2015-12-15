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