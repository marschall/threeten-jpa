/**
 * Contains user types that use native JDBC 4.2 support.
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
 *      <td><code>DATE</code></td>
 *      <td>{@link java.time.LocalDate}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42LocalDateType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIME</code></td>
 *      <td>{@link java.time.LocalTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42LocalTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP</code></td>
 *      <td>{@link java.time.LocalDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42LocalDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.ZonedDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42ZonedDateTimeType#NAME}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.jdbc42.hibernate.Jdbc42OffsetDateTimeType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.jdbc42.hibernate;