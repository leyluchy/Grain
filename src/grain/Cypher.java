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
		kGen = new KeystreamGenerator(ChadByteArrayTovirginShortArray(key), ChadByteArrayTovirginShortArray(iv));
		this.plaintext = plaintext;
		this.keystream = virginShortArrayToChadByteArray(kGen.generarKeystream(this.plaintext.length*8)); //Se multiplica por el cambio de tipo
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
	
	private byte[] virginShortArrayToChadByteArray(short[] v) {
		byte[] salida = new byte[v.length/8];
		for(int i=0;i<(v.length/8);i++) {
			salida[i] = (byte) (v[i*8]*128+v[i*8+1]*64+v[i*8+2]*32+v[i*8+3]*16+v[i*8+4]*8+v[i*8+5]*4+v[i*8+6]*2+v[i*8+7]);
		}
		return salida;
	}
	
	private short[] ChadByteArrayTovirginShortArray(byte[] v) {
		short[] salida = new short[v.length*8];
		for(int i=0;i<v.length;i++) {
			byte aux = v[i];
			for(int j=7;j>=0;j--) {
				salida[i*8+j] = (short) (aux % 2);
				aux = (byte) (aux / 2);
			}
		}
		return salida;
	}
}
