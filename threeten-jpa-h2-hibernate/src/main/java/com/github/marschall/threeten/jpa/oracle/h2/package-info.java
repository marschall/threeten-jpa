/**
 * Contains additional user type converters for H2.
 * <p>
 * The H2 driver module has to be visible to the deployment
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
 *      <td>OffsetDateTime</td>
 *      <td>{@value com.github.marschall.threeten.jpa.oracle.h2.H2OffsetDateTimeType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.oracle.h2;