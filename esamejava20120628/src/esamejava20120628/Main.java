package esamejava20120628;

public class Main {
	final static int n = 10;
	final static int max = 5;
	
	public static void main(String args[]) {
		System.out.println("***************** AVVIO ESECUZIONE *****************");
		Ponte ponte = new Ponte(max);
		Visitatori []visitatore = new Visitatori[n];
		for(int i =0 ;i<n;i++){
			visitatore[i] = new Visitatori(ponte, i);
		}
		for(int i =0 ;i<n;i++){
			visitatore[i].start();
		}
	}
	
	
}