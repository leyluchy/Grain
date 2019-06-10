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
		this.keystream = virginShortArrayToChadByteArray(kGen.generarKeystream(this.plaintext.length-54)); //Se multiplica por el cambio de tipo
		if((this.largo = this.plaintext.length)-54!=this.keystream.length)
			throw new LargosDiferentesException();
	}
	
	/**
	 * Método que se encarga de realizar el xoreo entre el texto plano y el keystream
	 * @return array de bytes ya cifrados
	 */
	public byte[] getXored() {
		byte[] tCifrado = this.xorArray();
		return tCifrado;
	}
	
	/**
	 * Método que se encarga de xorear elemento por elemento
	 * @param x1 uno de las listas de bytes a xorear
	 * @param x2 otra de las listas a de bytes a xorear
	 * @return lista de elementos xoreados
	 */
	private byte[] xorArray() {
		byte[] tCifrado = new byte[this.largo];
		byte[] nh = getNotHeader(this.plaintext);
		byte[] h = getHeader(this.plaintext);
		for(int i=0;i<54;i++)
			tCifrado[i]=h[i];
		for (int i=0; i<this.largo-54; i++) {
			tCifrado[i+54] = (byte) ((nh[i]) ^ this.keystream[i]);
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
	
	private byte[] getHeader(byte[] b) {
		byte[] cabecera = new byte[54];
		for(int i=0;i<54;i++)
			cabecera[i]=b[i];
		return cabecera;
	}
	
	private byte[] getNotHeader(byte[] b) {
		int largo = b.length-54;
		byte[] toXor = new byte[largo];
		for(int i=54;i<largo;i++)
			toXor[i]=b[i];
		return toXor;
	}
}
