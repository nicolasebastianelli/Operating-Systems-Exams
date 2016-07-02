package Aeroporto;
import java.util.Date;
import java.util.Random;

public class Aereo extends Thread {
	
	private Pista p;
	private Random r;
	private Date d;
	
	public Aereo(Pista p, Random r) {
		this.p = p;
		this.r = r;
	}
	
	public void run() {
		boolean atterraggo = r.nextBoolean();
		try {
			sleep(r.nextInt(50000) + 1000);
			
			if (atterraggo)
				p.atterraggio();
			else
				p.decollo();
			
			sleep(500);
			this.d = new Date();
			p.fineOperazioni(false);
			
			if (atterraggo)
				System.out.println("atterraggio " + generateId(r) + " " + d.toString());
			else
				System.out.println("decollo " + generateId(r) + " " + d.toString());
			
			p.printQueue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String generateId(Random r) {
		char[] alphabeth = new char[26];
		char c = 'A';
		for (int i = 0; i < alphabeth.length; i++) {
			alphabeth[i] = c;
			c++;
		}
		return  "" + alphabeth[r.nextInt(26)] + alphabeth[r.nextInt(26)] + (r.nextInt(900) + 100);
	}
}
