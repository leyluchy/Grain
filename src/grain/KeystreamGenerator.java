package grain;

public class KeystreamGenerator {
	private byte[] key, iv, lfsr, nfsr;

	/**
	 * 
	 * @param key clave para cifrar
	 * @param iv semilla para keystream
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 */
	public KeystreamGenerator(byte[] key, byte[] iv) throws MuchosOPocosBytesException {
		//Levanto key
		if(key.length == 80/8) {
			this.key = new byte[80/8];
			for(int i=0; i < this.key.length; i++)
				this.key[i] = key[i];
		}
		else throw new MuchosOPocosBytesException();
		
		//Levanto iv
		if(iv.length == 64/8) {
			this.iv = new byte[64/4];
			for(int i=0; i < this.iv.length; i++)
				this.iv[i] = iv[i];
		}
		else throw new MuchosOPocosBytesException();
		
		//Creo LFSR y NFSR
		this.lfsr = new byte[80/8];
		this.nfsr = new byte[80/8];
		
		inicializar();
		
	}
	
	private void inicializar() {
		
	}
	
	/**
	 * Genera la siguiente parte del keystream para cifrar el mensaje.
	 * @param cantBytes Cantidad en bytes de keystream a generar
	 * @return Keystream generado
	 */
	public byte[] generarKeystream(int cantBytes) {
		return null;
		
	}
	
}
