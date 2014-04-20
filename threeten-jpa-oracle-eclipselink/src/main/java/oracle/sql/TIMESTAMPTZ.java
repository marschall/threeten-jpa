package oracle.sql;


/**
 * Dummy {@code TIMESTAMPTZ} interface which avoids a dependency to OJDBC.
 * 
 * <p>
 * Will be removed from final JAR.
 */
public class TIMESTAMPTZ {
  
  public TIMESTAMPTZ(byte[] timestamptz) {
    super();
  }

  public byte[] toBytes() {
    return new byte[0];
  }

}
