package Aeroporto;
import java.util.Random;


public class TorreControllo {
	public final static int MAX = 5;
	public final static int numAerei = 100;
	public final static int numMezzi = 50;
	
	public static void main(String[] args) {
		Pista p = new Pista(MAX);
		Random r = new Random(System.currentTimeMillis());
		Aereo[] aerei = new Aereo[numAerei];
		MezzoServizio[] mezzi = new MezzoServizio[numMezzi];
		
		for (int i = 0; i < numAerei; i++)
			aerei[i] = new Aereo(p, r);
		
		for (int i = 0; i < numMezzi; i++)
			mezzi[i] = new MezzoServizio(p, r);
		
		for (int i = 0; i < numAerei; i++)
			aerei[i].start();
		
		for (int i = 0; i < numMezzi; i++)
			mezzi[i].start();
	}
	

}
