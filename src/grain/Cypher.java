package grain;

public class Cypher {
	private byte[] plaintext, keystream;
	private int largo;
	private KeystreamGenerator kGen;

	/**
	 * Crea e inicializa el cifrador
	 * @param key clave en bytes para cifrar
	 * @param iv semilla en bytes para keystream
	 * @param plaintext texto en bytes a encriptar/desencriptar
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 * @throws LargosDiferentesException si difieren largo de keystream y plaintext por algun error interno
	 */
	public Cypher(byte[] key, byte[] iv, byte[] plaintext) throws MuchosOPocosBytesException, LargosDiferentesException {
		//Paso key e iv a short[] y creo el keystream generator
		kGen = new KeystreamGenerator(ChadByteArrayTovirginShortArray(key), ChadByteArrayTovirginShortArray(iv));
		
		this.plaintext = plaintext;
		
		//Genero el keystream para todo menos la cabecera del .bmp
		//Paso el keystream a byte[]
		this.keystream = virginShortArrayToChadByteArray(kGen.generarKeystream(this.plaintext.length-54)); //Se multiplica por el cambio de tipo
		
		//Si hay algun error entre largos de keystream y .bmp sin cabecera, exception
		if((this.largo = this.plaintext.length)-54!=this.keystream.length)
			throw new LargosDiferentesException();
	}
	
	/**
	 * Método que se encarga de xorear elemento por elemento
	 * XOR entre plaintext sin header y keystream
	 * @return lista de elementos xoreados
	 */
	public byte[] getXored() {
		byte[] tCifrado = new byte[this.largo];
		byte[] nh = getNotHeader(this.plaintext);
		byte[] h = getHeader(this.plaintext);
		
		//Paso el header sin cifrar
		for(int i=0;i<54;i++)
			tCifrado[i]=h[i];
		
		//Cifro el resto de la imagen
		//System.out.println("IMPRESION!!!!!!!!");
		for (int i=0; i<this.largo-54; i++) {
			tCifrado[i+54] = (byte) ((nh[i]) ^ this.keystream[i]);
			//System.out.println("PT: "+nh[i]+" KS: "+this.keystream[i]+" Xred: "+tCifrado[i+54]);
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
		for(int i=0;i<largo;i++)
			toXor[i]=b[i+54];
		return toXor;
	}
}
