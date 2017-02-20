package oracle.sql;

public class RAW {

  private byte[] rawBytes;

  public RAW(byte[] rawBytes) {
    this.rawBytes = rawBytes;
  }

  public byte[] getBytes() {
    return this.rawBytes.clone();
  }

  public byte[] shareBytes() {
    return this.rawBytes;
  }

}
