/**
 * Contains additional type converters for Oracle that currently only
 * work with EclipseLink.
 * <p>
 * All the converters have set
 * {@link javax.persistence.Converter#autoApply()} to {@code true} to
 * they're automatically applied to all entities in the same persistence
 * unit.
 * <p>
 * The Oracle driver module has to be visible to the deployment
 * (eg <a href="https://docs.jboss.org/author/display/WFLY8/Class+Loading+in+WildFly">Class Loading in WildFly</a>).
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
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.OracleOffsetDateTimeConverter}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.oracle;