package esercizio10;

import java.util.Random;

public class Collezionista extends Thread {
	private Deposito d;
	private int offerta, richiesta, max;
	private int[] carte;
	private Random r = new Random();

	public Collezionista(Deposito d, int nf) {
		super();
		this.d = d;
		this.max = nf;
		carte = new int[max];
		System.out.printf("[" + getName() + "] carte disponibili: ");
		for (int i = 0; i < max; i++) {
			carte[i] = r.nextInt(3);
			System.out.printf(" " + carte[i] + " ");			
		}
		System.out.println();
	}

	public void run() {
		try {
			Thread.sleep(1000);
			while (true) {

				while (carte[offerta = r.nextInt(max)] == 0)
					;
				richiesta = r.nextInt(max);
				System.out.println("[" + getName() + "] carta richiesta: " + richiesta + ", carta offerta: " + offerta);
				d.scambia(offerta, richiesta);
				offerta--;
				richiesta++;
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
		}

	}
}
