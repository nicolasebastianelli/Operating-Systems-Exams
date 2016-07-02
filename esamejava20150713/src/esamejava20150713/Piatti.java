package esamejava20150713;

import java.util.Random;

public class Piatti extends Thread{
	
	private Barbeque b;
	private int id;
	private Random r;
	private final static int PN = 1;
	private final static int PSG = 0;
	
	public Piatti(Barbeque b, int id) {
		super();
		this.b = b;
		this.id = id;
		r= new Random(System.currentTimeMillis()*id);
	}
	
	public void run()
	{
		int tipo;
		tipo=r.nextInt(2);
		if(tipo ==PN)
		{
			System.out.println("[Piatti]: Ricevuto ordine per piatto normale con id: "+id);
		}
		else{
			System.out.println("[Piatti]: Ricevuto ordine per piatto senza glutine con id: "+id);
		}
		while(true){
		try {
			b.inizioPiatto(tipo, id);
			Thread.sleep(2000);
			b.finePiatto(tipo, id);
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	

}
