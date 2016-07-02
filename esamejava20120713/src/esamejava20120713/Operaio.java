package esamejava20120713;

public class Operaio extends Thread{
	private Cesto c;

	public Operaio(Cesto c)  {
		super();
		this.c = c;
	}
	public void run(){
		while(true)
		{
			
			try {
				c.inscatola();
				Thread.sleep(1000);			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
