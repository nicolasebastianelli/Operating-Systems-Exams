package esamejava20140627;

public class Avvio {
	final static int n = 5;
	final static int ng = 10;

	public static void main(String args[]) {
		System.out.println("***************** AVVIO ESECUZIONE *****************");
		Noleggio noleggio= new Noleggio(n);
		Gruppi []g = new Gruppi[ng];
		Addetto a = new Addetto(noleggio);
		for(int i=0;i<ng;i++)
		{
			g[i]= new Gruppi(noleggio,i);
		}
		for(int i=0;i<ng;i++)
		{
			g[i].start();
		}
		a.start();
	}
}
