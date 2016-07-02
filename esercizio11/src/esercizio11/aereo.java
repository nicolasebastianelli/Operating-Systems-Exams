package esercizio11;

import java.util.Random;

public class aereo extends Thread {
	private int numero;
	private pista pista;
	private Random r ;

	public aereo(int numero, pista p, Random r) {
		super();
		this.numero = numero;
		this.pista = p;
		this.r=r;
	}

	public void run() {

		try {
			if (r.nextBoolean())
				pista.decollo(numero);
			else
				pista.atterraggio(numero);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
