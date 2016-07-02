package esercizio11;

import java.util.Random;

public class Main {
	final static int max=3;
	public static void main (String args[]) {		
		Random r= new Random(System.currentTimeMillis());
		pista p = new pista(max);
		aereo []a = new aereo[10];
		servizi[]s = new servizi[10];
		for(int i=0; i<10;i++){
			a[i] = new aereo(i, p,r);
		}
		for(int i=0; i<10;i++){
			s[i] = new servizi(p, i);
		}
		for(int i=0; i<10;i++){
			a[i].start();
		}
		for(int i=0; i<10;i++){
			s[i].start();
		}
	}
}