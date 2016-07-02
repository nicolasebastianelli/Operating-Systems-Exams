package esamejava20110628;


public class Main {
	final static int maxn = 10;
	final static int maxcab = 5;
	final static int ns = 25;
	final static int ng = 5;
	
	public static void main(String args[]) {
		System.out.println("***************** AVVIO ESECUZIONE *****************");
		Spogliatoio spogliatoio = new Spogliatoio(maxn,maxcab);
		Singoli []singoli = new Singoli[ns];
		Gruppi []gruppi = new Gruppi[ng];
		for(int i =0 ;i<ns;i++){
			singoli[i] = new Singoli(spogliatoio,i);
		}
		for(int i =0 ;i<ng;i++){
			gruppi[i] = new Gruppi(spogliatoio,i);
		}
		for(int i =0 ;i<ns;i++){
			singoli[i].start();
		}
		for(int i =0 ;i<ng;i++){
			gruppi[i].start();
		}
	}
	
	
}