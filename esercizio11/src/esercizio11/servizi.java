package esercizio11;


public class servizi extends Thread{
	pista p;
	int n;
	public servizi( pista p, int n) {
		super();
		this.p = p;
		this.n = n;
	}
	public void run() {
			try {
				p.servizi(n);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}

