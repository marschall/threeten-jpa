/**
 * Contains additional user type converters for Oracle.
 * <p>
 * The Oracle driver module has to be visible to the deployment
 * (eg <a href="https://docs.jboss.org/author/display/WFLY10/Class+Loading+in+WildFly">Class Loading in WildFly</a>).
 * <table>
 *  <caption>Supported type conversions</caption>
 *  <thead>
 *    <tr>
 *      <td>ANSI SQL</td>
 *      <td>Java SE 8</td>
 *      <td>Converter</td>
 *     </tr>
 *  </thead>
 *  <tbody>
 *    <tr>
 *      <td>TIMESTAMP WITH TIMEZONE</td>
 *      <td>{@link java.time.ZonedDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleZonedDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td>TIMESTAMP WITH TIMEZONE</td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleOffsetDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td>TIMESTAMP</td>
 *      <td>{@link java.time.LocalDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleLocalDateTimeType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.oracle.hibernate;