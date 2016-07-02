package esercizio10;


public class Main {
	
	
		public static void main (String args[]) {
			final int NF=10;
			final int NT=5;
			int i;
			System.out.println("************** Inizio esecuzione ******************");
			Collezionista []collezionista= new Collezionista[NF];
			Deposito D = new Deposito(NF);
			for (i = 0; i < NT; i++) {
				collezionista[i] = new Collezionista(D,NF);
				collezionista[i].start();
			}
			/*
			 * NB: Date le specifiche dell'esercizio, non esiste modo di evitare che a un certo punto vi sia un deadlock: 
			 * per esempio, se tutti i processi partono cercando di prelevare e nessuno versa...
			 */
		}

}
