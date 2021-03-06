/**
 * Contains additional classed for H2 and EclipseLink.
 *
 * <h3>Attribute Converters</h3>
 * Contains additional attribute converters for H2 that currently only
 * work with EclipseLink.
 * <p>
 * All the converters have set
 * {@link javax.persistence.Converter#autoApply()} to {@code true} to
 * they're automatically applied to all entities in the same persistence
 * unit.
 * <p>
 * The H2 driver module has to be visible to the deployment
 * (eg <a href="https://docs.jboss.org/author/display/WFLY/Class+Loading+in+WildFly">Class Loading in WildFly</a>).
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
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.h2.H2OffsetDateTimeConverter}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.h2;