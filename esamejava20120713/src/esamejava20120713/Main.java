package esamejava20120713;

public class Main {
	final static int n = 4;
	final static int max = 8;

	public static void main(String args[]) {
		System.out.println("***************** AVVIO ESECUZIONE *****************");
		System.out.println("[MAIN]: Numero massino per ogni tipo di scarpa: "+max/4);
		Cesto cesto = new Cesto(max);
		Calzolaio []calzolaio = new Calzolaio[n];
		Operaio operaio = new Operaio(cesto);
		for(int i =0 ;i<n;i++){
			calzolaio[i] = new Calzolaio(cesto, i);
		}
		operaio.start();
		for(int i =0 ;i<n;i++){
			calzolaio[i].start();
		}
		
	}		
}
