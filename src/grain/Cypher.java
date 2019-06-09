package grain;

public class Cypher {
	private byte[] plaintext, keystream;
	private int largo;
	private KeystreamGenerator kGen;

	/**
	 * Crea el Generador de claves y obtiene el keystream
	 * @param key clave para cifrar
	 * @param iv semilla para keystream
	 * @param plaintext texto a encriptar/desencriptar
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 */
	public Cypher(byte[] key, byte[] iv, byte[] plaintext) throws MuchosOPocosBytesException, LargosDiferentesException {
		kGen = new KeystreamGenerator(key, iv);
		this.plaintext = plaintext;
		this.keystream = kGen.generarKeystream(this.plaintext.length);
		if((this.largo = this.plaintext.length)!=this.keystream.length)
			throw new LargosDiferentesException();
	}
	
	/**
	 * Método que se encarga de realizar el xoreo entre el texto plano y el keystream
	 * @return array de bytes ya cifrados
	 */
	public byte[] getXored() {
		byte[] tCifrado = this.xorArray(this.plaintext,this.keystream);
		return tCifrado;
	}
	
	/**
	 * Método que se encarga de xorear elemento por elemento
	 * @param x1 uno de las listas de bytes a xorear
	 * @param x2 otra de las listas a de bytes a xorear
	 * @return lista de elementos xoreados
	 */
	private byte[] xorArray(byte[] x1, byte[] x2) {
		byte[] tCifrado = new byte[this.largo];
		for (int i=0; i<this.largo; i++) {
			tCifrado[i] = (byte) (this.plaintext[i] ^ this.keystream[i]);
		}
		return tCifrado;
	}
	
}
