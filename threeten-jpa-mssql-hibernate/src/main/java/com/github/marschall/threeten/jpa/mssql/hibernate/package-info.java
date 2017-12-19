/**
 * Contains additional user types for  SQL Server.
 * <p>
 * The  SQL Server driver module has to be visible to the deployment
 * (eg <a href="https://docs.jboss.org/author/display/WFLY/Class+Loading+in+WildFly">Class Loading in WildFly</a>).
 * </p>
 * <table>
 *  <caption>Supported type conversions</caption>
 *  <thead>
 *    <tr>
 *      <td>Transact-SQL</td>
 *      <td>Java SE 8</td>
 *      <td>Name</td>
 *     </tr>
 *  </thead>
 *  <tbody>
 *    <tr>
 *      <td><code>DATETIMEOFFSET</code></td>
 *      <td>{@link java.time.OffsetDateTime}</td>
 *      <td>{@value com.github.marschall.threeten.jpa.mssql.hibernate.MssqlOffsetDateTimeType#NAME}</td>
 *    </tr>
 *  </tbody>
 * </table>
 */
package com.github.marschall.threeten.jpa.mssql.hibernate;