/******************************************
Developer: Subhadeep Banik
email : s.banik_r@isical.ac.in
*******************************************/

// In this implementation of GRAIN the most significant bit of the 1st hex value is treated as index 0
#include<stdio.h>
#include<conio.h>

main()
{
	int lfsr[80],nfsr[80],tl,tn,zr[80];
	static char ki[21],IV[17];
	int t,tt,i,ie1,ie2,ie,a0;
	int op;



	printf("Enter Initial Key in HEX 20 bit :");
	scanf("%s",ki);
	// ki stores the secret key
	printf("Enter Initial vector in HEX 16 bit :");
	scanf("%s",IV);
	// IV stores the initial vector
	

	// convert the string ki to binary and store it in the NFSR
	for(i=0;i<20;i+=2)
	{
		if(ki[i]>=’0’ && ki[i]<=’9’) 
			ie =ki[i]-’0’;
		else if (ki[i]>=’a’ && ki[i]<=’f’) 
			ie=ki[i]-’a’+10;
		else if (ki[i]>=’A’ && ki[i]<=’F’) 
			ie=ki[i]-’A’+10;

		a0=ie/8; ie=ie%8; nfsr[4*i]=a0;
		a0=ie/4; ie=ie%4; nfsr[4*i+1]=a0;
		a0=ie/2; ie=ie%2; nfsr[4*i+2]=a0;
		a0=ie; nfsr[4*i+3]=a0;

		if(ki[i+1]>=’0’ && ki[i+1]<=’9’) 
			ie =ki[i+1]-’0’;
		else if (ki[i+1]>=’a’ && ki[i+1]<=’f’) 
			ie=ki[i+1]-’a’+10;
		else if (ki[i+1]>=’A’ && ki[i+1]<=’F’) 
			ie=ki[i+1]-’A’+10;

		a0=ie/8; ie=ie%8; nfsr[4*i+4]=a0;
		a0=ie/4; ie=ie%4; nfsr[4*i+5]=a0;
		a0=ie/2; ie=ie%2; nfsr[4*i+6]=a0;
		a0=ie; nfsr[4*i+7]=a0;
	}
	

	// convert the string IV to binary and store it in the LFSR
	for(i=0;i<16;i+=2)
	{
		if(IV[i]>=’0’ && IV[i]<=’9’) 
			ie =IV[i]-’0’;
		else if (IV[i]>=’a’ && IV[i]<=’f’) 
			ie=IV[i]-’a’+10;
		else if (IV[i]>=’A’ && IV[i]<=’F’) 
			ie=IV[i]-’A’+10;

		a0=ie/8; ie=ie%8; lfsr[4*i]=a0;
		a0=ie/4; ie=ie%4; lfsr[4*i+1]=a0;
		a0=ie/2; ie=ie%2; lfsr[4*i+2]=a0;
		a0=ie; lfsr[4*i+3]=a0;

		if(IV[i+1]>=’0’ && IV[i+1]<=’9’) 
			ie =IV[i+1]-’0’;
		else if (IV[i+1]>=’a’ && IV[i+1]<=’f’) 
			ie=IV[i+1]-’a’+10;
		else if (IV[i+1]>=’A’ && IV[i+1]<=’F’) 
			ie=IV[i+1]-’A’+10;

		a0=ie/8; ie=ie%8; lfsr[4*i+4]=a0;
		a0=ie/4; ie=ie%4; lfsr[4*i+5]=a0;
		a0=ie/2; ie=ie%2; lfsr[4*i+6]=a0;
		a0=ie; lfsr[4*i+7]=a0;
	}

	
	// The last 16 bits of LFSR are initialized to ones to avoid all zero state
	for (i=64;i<80;i++)
		lfsr[i]=1;


	// initialisation process
	for(tt=0;tt<160;tt++){
		tl= (lfsr[0]+lfsr[13]+lfsr[23]+lfsr[38]+lfsr[51]+lfsr[62])%2;

		tn= (lfsr[0] + nfsr[62] + nfsr[60] + nfsr[52] + nfsr[45] + nfsr[37] + nfsr[33] + 
			nfsr[28] + nfsr[21] + nfsr[14] + nfsr[9] + nfsr[0] + 
			nfsr[63]*nfsr[60]+nfsr[37]*nfsr[33] + nfsr[15]*nfsr[9] + 
			nfsr[60]*nfsr[52]*nfsr[45]+nfsr[33]*nfsr[28]*nfsr[21] + 
			nfsr[63]*nfsr[45]*nfsr[28]*nfsr[9] + nfsr[60]*nfsr[52]*nfsr[37]*nfsr[33] + 
			nfsr[63]*nfsr[60] + nfsr[63]*nfsr[60]*nfsr[52]*nfsr[45]*nfsr[37] + 
			nfsr[33]*nfsr[28]*nfsr[21]*nfsr[15]*nfsr[9] + 
			nfsr[52]*nfsr[45]*nfsr[37]*nfsr[33]*nfsr[28]*nfsr[21])%2;

		op= (nfsr[1] + nfsr[2] + nfsr[4] + nfsr[10] + nfsr[31] + nfsr[43] + nfsr[56] + 
			lfsr[25] + nfsr[63] + lfsr[3]*lfsr[64] + lfsr[46]*lfsr[64] +
			lfsr[64]*nfsr[63] + lfsr[3]*lfsr[25]*lfsr[46] + lfsr[3]*lfsr[46]*lfsr[64] + 
			lfsr[3]*lfsr[46]*nfsr[63] + lfsr[25]*lfsr[46]*nfsr[63] +
			lfsr[46]*lfsr[64]*nfsr[63] )%2 ;

		for(i=0;i<=78;i++)
			lfsr[i]=lfsr[i+1];
		lfsr[79]=(tl+op)%2;

		for(i=0;i<=78;i++)
			nfsr[i]=nfsr[i+1];
		nfsr[79]=(tn+op)%2;
	}


	// Stream generation for the first 80 clocks
	for(tt=0;tt<80;tt++)
	{
		tl= (lfsr[0] + lfsr[13] + lfsr[23] + lfsr[38] + lfsr[51] + lfsr[62])%2;

		tn= (lfsr[0] + nfsr[62] + nfsr[60] + nfsr[52] + nfsr[45] + nfsr[37] + nfsr[33] + 
			nfsr[28] + nfsr[21] + nfsr[14] + nfsr[9] + nfsr[0] + nfsr[63]*nfsr[60] + 
			nfsr[37]*nfsr[33] + nfsr[15]*nfsr[9] + nfsr[60]*nfsr[52]*nfsr[45] + 
			nfsr[33]*nfsr[28]*nfsr[21] + nfsr[63]*nfsr[45]*nfsr[28]*nfsr[9] + 
			nfsr[60]*nfsr[52]*nfsr[37]*nfsr[33] + nfsr[63]*nfsr[60]*nfsr[21]*nfsr[15] + 
			nfsr[63]*nfsr[60]*nfsr[52]*nfsr[45]*nfsr[37] + 
			nfsr[33]*nfsr[28]*nfsr[21]*nfsr[15]*nfsr[9] + 
			nfsr[52]*nfsr[45]*nfsr[37]*nfsr[33]*nfsr[28]*nfsr[21])%2;

		// zr stores output
		zr[tt]= (nfsr[1] + nfsr[2] + nfsr[4] + nfsr[10] + nfsr[31] + nfsr[43] + 
			nfsr[56] + lfsr[25] + nfsr[63] + lfsr[3]*lfsr[64] + lfsr[46]*lfsr[64] + 
			lfsr[64]*nfsr[63] + lfsr[3]*lfsr[25]*lfsr[46] + lfsr[3]*lfsr[46]*lfsr[64] + 
			lfsr[3]*lfsr[46]*nfsr[63] +	lfsr[25]*lfsr[46]*nfsr[63] + 
			lfsr[46]*lfsr[64]*nfsr[63] )%2;

		for(i=0;i<=78;i++)
			lfsr[i]=lfsr[i+1];
		lfsr[79]=tl;

		for(i=0;i<=78;i++)
			nfsr[i]=nfsr[i+1];
		nfsr[79]=tn;
	}


	// print zr in hex
	printf("\n");
	for(t=0;t<80;t+=8)
	{
		ie1 = zr[t+3] + zr[t+2]*2 + zr[t+1]*4 + zr[t]*8;
		ie2 = zr[t+7] + zr[t+6]*2 + zr[t+5]*4 + zr[t+4]*8;
		printf("%x%x",ie1,ie2);
	}
	getch();
}