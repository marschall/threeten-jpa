/**
 * Contains composite types to support {@link java.time.ZonedDateTime}.
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
 *      <td><code>TIMESTAMP WITH TIME ZONE</code>, <code>VARCHAR</code></td>
 *      <td>{@link java.time.ZonedDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.zoned.hibernate.ZonedDateTimeType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.zoned.hibernate;