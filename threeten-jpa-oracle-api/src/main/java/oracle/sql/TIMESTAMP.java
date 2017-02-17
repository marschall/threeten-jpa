package oracle.sql;


/**
 * Dummy {@code TIMESTAMP} interface which avoids a dependency to OJDBC.
 */
public class TIMESTAMP {

  private byte[] timestamp;

  public TIMESTAMP(byte[] timestamp) {
    this.timestamp = timestamp;
  }

  public byte[] toBytes() {
    return this.timestamp;
  }

}
