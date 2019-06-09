package grain;

public class KeystreamGenerator {
	private short[] key, iv, lfsr, nfsr;

	/**
	 * Crea e inicializa un generador de keystream.
	 * @param key clave para cifrar, expresada en array de short donde cada uno es un 1 o un 0
	 * @param iv semilla para keystream, expresada en array de short donde cada uno es un 1 o un 0
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 */
	public KeystreamGenerator(short[] key, short[] iv) throws MuchosOPocosBytesException {
		//Levanto key
		if(key.length == 80) {
			this.key = new short[80];
			for(int i=0; i < this.key.length; i++)
				this.key[i] = key[i];
		}
		else throw new MuchosOPocosBytesException("Clave tiene " + key.length + "bits y tiene que tener 80.");
		
		//Levanto iv
		if(iv.length == 64) {
			this.iv = new short[64];
			for(int i=0; i < this.iv.length; i++)
				this.iv[i] = iv[i];
		}
		else throw new MuchosOPocosBytesException("IV tiene " + iv.length + "bits y tiene que tener 64.");
		
		//Creo LFSR y NFSR
		this.lfsr = new short[80];
		this.nfsr = new short[80];
		
		inicializar();
		
	}
	
	/**
	 * Inicializa el sistema
	 */
	private void inicializar() {
		int i;
		//Inicio NFSR con key
		for(i=0; i < key.length; i++)
			nfsr[i] = key[i];
		
		//Inicio LFSR con IV y relleno con 1
		for(i=0; i < iv.length; i++)
			lfsr[i] = iv[i];
		for(; i < lfsr.length; i++)
			lfsr[i] = 1;
		
		//Clockeo 160 veces
		for(i=0; i < 160; i++)
			initClock();
	}
	
	/**
	 * Genera la siguiente parte del keystream para cifrar el mensaje.
	 * @param cantBytes Cantidad en bytes de keystream a generar
	 * @return Keystream generado, expresado en un array de shorts donde cada uno es 1 o 0
	 */
	public short[] generarKeystream(int cantBytes) {
		short[] keystream = new short[cantBytes * 8];
		
		for(int i=0; i < keystream.length; i++)
			keystream[i] = clock();
		
		return keystream;
		
	}
	
	/**
	 * Ejecuta un clock normal
	 * @return El siguiente bit de keystream
	 */
	private short clock() {
		short output = outputFunction();
		shiftNFSR(feedbackNFSR()); //Esta va primero porque el feedback usa un bit del LFSR actual
		shiftLFSR(feedbackLFSR());
		return output;
	}
	
	/**
	 * Ejecuta un clock de inicializacion
	 */
	private void initClock() {
		short output = outputFunction();
		shiftNFSR((short)((feedbackNFSR() + output) % 2)); //Esta va primero porque el feedback usa un bit del LFSR actual
		shiftLFSR((short)((feedbackLFSR() + output) % 2));
	}
	
	/**
	 * Ejecuta la funcion de feedback de LFSR
	 * @return El bit con que se debe retroalimentar el LFSR
	 */
	private short feedbackLFSR() {
		return (short) ((lfsr[0] + lfsr[13] + lfsr[23] + lfsr[38] + lfsr[51] + lfsr[62]) % 2);
	}
	
	/**
	 * Corre todos los bits del LFSR una posicion a la izquierda
	 * @param newBit El bit que se debe colocar en la posicion que queda libre
	 */
	private void shiftLFSR(short newBit) {
		for(int i = 0; i <= lfsr.length - 1; i++)
			lfsr[i]=lfsr[i+1];
		lfsr[lfsr.length - 1] = newBit;
	}
	
	/**
	 * Ejecuta la funcion de feedback del NFSR.
	 * Usa un bit del LFSR en su estado actual.
	 * @return El bit con que se debe retroalimentar el NFSR
	 */
	private short feedbackNFSR() {
		return (short) ((
				lfsr[0] + nfsr[62] + nfsr[60] + nfsr[52] + nfsr[45] + nfsr[37] + nfsr[33] + 
				nfsr[28] + nfsr[21] + nfsr[14] + nfsr[9] + nfsr[0] + 
				nfsr[63]*nfsr[60]+nfsr[37]*nfsr[33] + nfsr[15]*nfsr[9] + 
				nfsr[60]*nfsr[52]*nfsr[45]+nfsr[33]*nfsr[28]*nfsr[21] + 
				nfsr[63]*nfsr[45]*nfsr[28]*nfsr[9] + nfsr[60]*nfsr[52]*nfsr[37]*nfsr[33] + 
				nfsr[63]*nfsr[60] + nfsr[63]*nfsr[60]*nfsr[52]*nfsr[45]*nfsr[37] + 
				nfsr[33]*nfsr[28]*nfsr[21]*nfsr[15]*nfsr[9] + 
				nfsr[52]*nfsr[45]*nfsr[37]*nfsr[33]*nfsr[28]*nfsr[21]
				) % 2);
	}
	
	/**
	 * Mueve todos los bits del NFSR una posicion a la izquierda.
	 * @param newBit El bit que se debe colocar en la posicion libre
	 */
	private void shiftNFSR(short newBit) {
		for(int i = 0; i <= nfsr.length - 1; i++)
			nfsr[i]=nfsr[i+1];
		nfsr[nfsr.length - 1] = newBit;
	}
	
	/**
	 * Ejecuta la funcion final que genera un bit mas del keystream.
	 * Incluye la funcion de filtro h(x)
	 * Combina bits del NFSR y LFSR en el estado actual.
	 * @return El nuevo bit del keystream.
	 */
	private short outputFunction() {
		return (short) ((
				//Esta linea es la sumatoria de bi
				nfsr[1] + nfsr[2] + nfsr[4] + nfsr[10] + nfsr[31] + nfsr[43] + nfsr[56] +
				//Lo que sigue es la filter function h(x)
				lfsr[25] + nfsr[63] + lfsr[3]*lfsr[64] + lfsr[46]*lfsr[64] +
				lfsr[64]*nfsr[63] + lfsr[3]*lfsr[25]*lfsr[46] + lfsr[3]*lfsr[46]*lfsr[64] + 
				lfsr[3]*lfsr[46]*nfsr[63] + lfsr[25]*lfsr[46]*nfsr[63] +
				lfsr[46]*lfsr[64]*nfsr[63] 
				) % 2);
	}
	
}
