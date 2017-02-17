package oracle.sql;


/**
 * Dummy {@code TIMESTAMPTZ} interface which avoids a dependency to OJDBC.
 *
 * @see <a href="https://docs.oracle.com/database/121/JAJDB/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a>
 */
public class TIMESTAMPTZ {

  private byte[] timestamptz;

  public TIMESTAMPTZ(byte[] timestamptz) {
    this.timestamptz = timestamptz;
  }

  public byte[] toBytes() {
    return this.timestamptz;
  }

}
