package oracle.sql;

/**
 * Dummy {@code INTERVALYM} interface which avoids a dependency to OJDBC.
 *
 * @see <a href="https://docs.oracle.com/database/121/JAJDB/oracle/sql/INTERVALYM.html">INTERVALYM</a>
 */
public class INTERVALYM {

  private byte[] intervalym;

  public INTERVALYM(byte[] intervalym) {
    this.intervalym = intervalym;
  }

  public byte[] toBytes() {
    return this.intervalym;
  }

}
