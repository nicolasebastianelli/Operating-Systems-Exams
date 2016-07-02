package esamejava20140627;

import java.util.Random;

public class Addetto extends Thread {
	private Noleggio n ;
	private Random r= new Random(System.currentTimeMillis());

	public Addetto(Noleggio n){
		super();
		this.n = n;
	}
	
	public void run(){
		while(true)
		{
			
			try {
				Thread.sleep(r.nextInt(2000)+5000);
				n.chiusura();
				Thread.sleep(r.nextInt(2000));
				n.apertura();
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}
