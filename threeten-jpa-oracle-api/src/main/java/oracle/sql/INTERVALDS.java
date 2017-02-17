package oracle.sql;

/**
 * Dummy {@code INTERVALDS} interface which avoids a dependency to OJDBC.
 */
public class INTERVALDS {

  private byte[] intervalds;

  public INTERVALDS(byte[] intervalds) {
    this.intervalds = intervalds;
  }

  public byte[] toBytes() {
    return this.intervalds;
  }

}
