package Aeroporto;
import java.util.Random;

public class MezzoServizio extends Thread {

	private Pista p;
	private Random r;

	public MezzoServizio(Pista p, Random r) {
		this.p = p;
		this.r = r;
	}

	public void run() {
		try {
			sleep(r.nextInt(10000));
			p.servizio();
			System.out.println("[" + getName() + "]: Entro in pista");
			sleep(250);
			p.fineOperazioni(true);
			p.printQueue();
			System.out.println("[" + getName() + "]: Esco dalla pista");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
