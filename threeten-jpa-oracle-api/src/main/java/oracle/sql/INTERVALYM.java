package oracle.sql;

public class INTERVALYM {

  private byte[] intervalym;

  public INTERVALYM(byte[] intervalym) {
    this.intervalym = intervalym;
  }

  public byte[] toBytes() {
    return this.intervalym;
  }

}
