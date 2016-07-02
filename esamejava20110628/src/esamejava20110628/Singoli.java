package esamejava20110628;

import java.util.Random;

public class Singoli extends Thread {
	
	final static int uomo =1;
	final static int donna =0;
	private Random r;
	private Spogliatoio s;
	private int id;
	public Singoli(Spogliatoio s, int id) {
		super();
		this.s = s;
		this.id = id;
		r= new Random(System.currentTimeMillis()*id);
	}
	
	public void run() {
		int genere;
		genere = r.nextInt(2);
		if(genere==uomo)
			System.out.println("[Singolo]: Creato uomo con id "+id);
		else
			System.out.println("[Singolo]: Creata donna con id "+id);
		while (true)
		{
			try {
				s.entraS(genere, id);
				Thread.sleep(2000);
				s.esceS(genere,id);
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

}
