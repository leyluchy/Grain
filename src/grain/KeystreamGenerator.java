package grain;

public class KeystreamGenerator {
	private short[] key, iv, lfsr, nfsr;

	/**
	 * 
	 * @param key clave para cifrar
	 * @param iv semilla para keystream
	 * @throws MuchosOPocosBytesException si key no tiene 80 bits o iv no tiene 64 bits
	 */
	public KeystreamGenerator(short[] key, short[] iv) throws MuchosOPocosBytesException {
		//Levanto key
		if(key.length == 80) {
			this.key = new short[80];
			for(int i=0; i < this.key.length; i++)
				this.key[i] = key[i];
		}
		else throw new MuchosOPocosBytesException();
		
		//Levanto iv
		if(iv.length == 64) {
			this.iv = new short[64];
			for(int i=0; i < this.iv.length; i++)
				this.iv[i] = iv[i];
		}
		else throw new MuchosOPocosBytesException();
		
		//Creo LFSR y NFSR
		this.lfsr = new short[80];
		this.nfsr = new short[80];
		
		inicializar();
		
	}
	
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
	 * @return Keystream generado
	 */
	public short[] generarKeystream(int cantBytes) {
		return null;
		
	}
	
	private short clock() {
		short output = outputFunction();
		shiftLFSR(feedbackLFSR());
		shiftNFSR(feedbackNFSR());
		return output;
	}
	
	private void initClock() {
		short output = outputFunction();
		shiftLFSR((short)((feedbackLFSR() + output) % 2));
		shiftNFSR((short)((feedbackNFSR() + output) % 2));
	}
	
	private short feedbackLFSR() {
		return (short) ((lfsr[0] + lfsr[13] + lfsr[23] + lfsr[38] + lfsr[51] + lfsr[62]) % 2);
	}
	
	private void shiftLFSR(short newBit) {
		for(int i = 0; i <= lfsr.length - 1; i++)
			lfsr[i]=lfsr[i+1];
		lfsr[lfsr.length - 1] = newBit;
	}
	
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
	
	private void shiftNFSR(short newBit) {
		for(int i = 0; i <= nfsr.length - 1; i++)
			nfsr[i]=nfsr[i+1];
		nfsr[nfsr.length - 1] = newBit;
	}
	
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
