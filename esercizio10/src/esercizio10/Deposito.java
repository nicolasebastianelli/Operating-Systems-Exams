package esercizio10;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deposito {
	private int[] FIGURINE;
	private int nfig;
	final private int Rare = 3;
	private Lock lock = new ReentrantLock();
	private Condition R = lock.newCondition();
	private Condition N = lock.newCondition();
	private int[] sospR;
	private int[] sospN;

	public Deposito(int n) {
		super();
		this.nfig = n;
		FIGURINE = new int[nfig];
		sospR = new int[100];
		sospN = new int[100];
		for (int i = 0; i < nfig; i++) {
			FIGURINE[i] = 1;
			sospR[i] = 0;
			sospN[i] = 0;
		}
	}

	public void scambia(int offerta,int richiesta) throws InterruptedException{
		Boolean fine = false;
		lock.lock();
		while(!fine)
		{
		if (FIGURINE[richiesta]!=0)
		{
			FIGURINE[richiesta]--;
			fine = true;
		}
		else{
			if(offerta<Rare)
			{
				sospR[richiesta]++;			
				R.await();				
				
			}
			else
			{
				sospN[richiesta]++;				
				N.await();							
			}
		}
		}
		FIGURINE[offerta]++;
		if(sospR[offerta] != 0){
			R.signalAll();
			sospR[offerta]=0;
		}
		else if (sospN[offerta] != 0)
		{
			N.signalAll();
			sospN[offerta]=0;
		}		
		System.out.printf("[Deposito] ");
		for(int i =0 ; i<nfig ; i++)
		System.out.printf(FIGURINE[i]+" ");
		System.out.println();
		lock.unlock();
	}
}
