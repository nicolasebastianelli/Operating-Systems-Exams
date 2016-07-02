package esamejava20150713;

public class Inserviente extends Thread{
	
	private Barbeque b;

	public Inserviente(Barbeque b) {
		super();
		this.b = b;
	}
	
	public void run()
	{
		while(true){
			try {
				b.inizioPulizia();
				Thread.sleep(4000);
				b.finePulizia();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
