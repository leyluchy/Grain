package grain;

public class Cypher {
	private byte[] plaintext, keystream;
  private KeystreamGenerator kGen;

	/**
	 * 
	 * @param key clave para cifrar
	 * @param iv semilla para keystream
   * @param plaintext texto a encriptar/desencriptar
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 */
	public Cypher(byte[] key, byte[] iv, byte[] plaintext) {
		
	}
}
