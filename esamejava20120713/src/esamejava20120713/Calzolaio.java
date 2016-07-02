package esamejava20120713;

import java.util.Random;

public class Calzolaio extends Thread{
	
	private Cesto c;
	private Random rp;
	private Random rm;
	private int i;

	public Calzolaio(Cesto c,int i) {
		super();
		this.c = c;
		this.i=i;
	}
	
	
	public void run(){
		while(true)
		{
			try {
				rm= new Random(System.currentTimeMillis()*i);
				rp= new Random(System.currentTimeMillis()*(i+1));
				int piede= rp.nextInt(2) +1;
				int misura =rm.nextInt(2)+1;
				System.out.println("[Calzolaio "+i+"] Ha creato scarpa piede: "+piede+" misura: "+misura);
				c.inserisci(piede, misura,i);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
