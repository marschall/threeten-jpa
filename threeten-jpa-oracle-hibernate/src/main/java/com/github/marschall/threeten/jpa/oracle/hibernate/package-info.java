/**
 * Contains additional user types for Oracle.
 * <p>
 * The Oracle driver module has to be visible to the deployment
 * (eg <a href="https://docs.jboss.org/author/display/WFLY/Class+Loading+in+WildFly">Class Loading in WildFly</a>).
 * </p>
 * <table>
 *  <caption>Supported type conversions</caption>
 *  <thead>
 *    <tr>
 *      <td>ANSI SQL</td>
 *      <td>Java SE 8</td>
 *      <td>Name</td>
 *     </tr>
 *  </thead>
 *  <tbody>
 *    <tr>
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.ZonedDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleZonedDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleOffsetDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP</code></td>
 *      <td>{@link java.time.LocalDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.hibernate.OracleLocalDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL YEAR TO MONTH</code></td>
 *      <td>{@link java.time.Period}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.hibernate.OraclePeriodType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL YEAR TO MONTH</code></td>
 *      <td>{@link java.time.Period}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.hibernate.OracleJdbc42PeriodType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL DAY TO SECOND</code></td>
 *      <td>{@link java.time.Duration}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.hibernate.OracleDurationType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL DAY TO SECOND</code></td>
 *      <td>{@link java.time.Duration}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.hibernate.OracleJdbc42DurationType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.oracle.hibernate;