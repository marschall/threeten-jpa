/**
 * Contains JPA type converters Java 8 Date and Time API types.
 * <p>
 * All the converters have set
 * {@link javax.persistence.Converter#autoApply()} to {@code true} to
 * they're automatically applied to all entities in the same persistence
 * unit.
 * <p>
 * We take inspiration from the JDBC 4.2 and currently support the following conversions:
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
 *      <td><code>DATE</code></td>
 *      <td>{@link java.time.LocalDate}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalDateConverter}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIME</code></td>
 *      <td>{@link java.time.LocalTime}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalTimeConverter}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP</code></td>
 *      <td>{@link java.time.LocalDateTime}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalDateTimeConverter}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa;
