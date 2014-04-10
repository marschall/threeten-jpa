/**
 * Contains JPA type converters Java 8 Date and Time API types.
 * <p>
 * All the converters have set
 * {@link javax.persistence.Converter#autoApply()} to {@code true} to
 * they're automatically applied to all entities in the same persistence
 * unit.
 * <p>
 * <table>
 *  <thead>
 *    <tr>
 *      <td>ANSI SQL</td>
 *      <td>Java SE 8</td>
 *      <td>Converter</td>
 *     </tr>
 *  </thead>
 *    <tr>
 *      <td>DATE</td>
 *      <td>LocalDate</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalDateConverter}</td>
 *    </tr>
 *    <tr>
 *      <td>TIME</td>
 *      <td>LocalTime</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalTimeConverter}</td>
 *    </tr>
 *    <tr>
 *      <td>TIMESTAMP</td>
 *      <td>LocalDateTime</td>
 *      <td>{@link com.github.marschall.threeten.jpa.LocalDateTimeConverter}</td>
 *    </tr>
 *  <tbody>
 *  </tbody>
 * <table>
 */
package com.github.marschall.threeten.jpa;
