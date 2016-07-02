package esamejava20140627;

import java.util.Random;

public class Gruppi extends Thread {
	private Random r;
	private Noleggio n;
	private int numero;
	private int i;

	public Gruppi(Noleggio n,int i) {
		super();
		this.n = n;
		this.i=i;
	}

	public void run() {
		while (true) {
			try {
				r= new Random(System.currentTimeMillis()*(i+1));
				numero = r.nextInt(8) + 1;
				n.prelievo(numero,i);
				Thread.sleep(5000);
				n.restituzione(numero,i);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
