/**
 * Contains additional classed for Oracle and EclipseLink.
 *
 * <h3>Attribute Converters</h3>
 * Contains additional attribute converters for Oracle that currently only
 * work with EclipseLink.
 * <p>
 * All the converters have set
 * {@link javax.persistence.Converter#autoApply()} to {@code true} to
 * they're automatically applied to all entities in the same persistence
 * unit.
 * <p>
 * The Oracle driver module has to be visible to the deployment
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
 *      <td>{@link java.time.ZonedDateTime}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.OracleZonedDateTimeConverter}</td>
 *    </tr>
 *    <tr>
 *      <td><code>TIMESTAMP WITH TIME ZONE</code></td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.OracleOffsetDateTimeConverter}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL YEAR TO MONTH</code></td>
 *      <td>{@link java.time.Period}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.OraclePeriodConverter}</td>
 *    </tr>
 *    <tr>
 *      <td><code>INTERVAL DAY TO SECOND</code></td>
 *      <td>{@link java.time.Duration}</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.OracleDurationConverter}</td>
 *    </tr>
 *  </tbody>
 * </table>
 *
 * <h3>EclipseLink Platforms</h3>
 * In addition as a work around for
 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=511999">Bug 511999</a>
 * we also provide the following platforms.
 *
 * <table>
 *  <caption>EclipseLink Platforms</caption>
 *  <thead>
 *    <tr>
 *      <td>Oracle Version</td>
 *      <td>Platform</td>
 *     </tr>
 *  </thead>
 *  <tbody>
 *    <tr>
 *      <td>12c</td>
 *      <td>{@link com.github.marschall.threeten.jpa.oracle.PatchedOracle12Platform}</td>
 *    </tr>
 *  </tbody>
 * </table>
 *
 *
 */
package com.github.marschall.threeten.jpa.oracle;