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
	public Cypher(byte[] key, byte[] iv, byte[] plaintext) throws MuchosOPocosBytesException {
		kGen = new KeystreamGenerator(key, iv);
		this.plaintext = plaintext;
		kGen.generarKeystream(this.plaintext.length);
	}
	
	/**
	 * Método que se encarga de realizar el cifrado
	 * @return
	 */
	public byte[] cifrar() {
		byte[] tCifrado;
		//TODO cifrado
		return tCifrado;
	}
	
}
