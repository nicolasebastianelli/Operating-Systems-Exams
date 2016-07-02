package Aeroporto;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Pista {
	private int MAX;
	
	private Lock lock = new ReentrantLock();
	private Condition atterraggi = lock.newCondition();
	private Condition decolli = lock.newCondition();
	private Condition servizio = lock.newCondition();
	
	private int sospAtterraggi;
	private int sospDecolli;
	private int sospMezzi;
	
	boolean occupato;

	private int numMezzi;
	
	public Pista(int max) {
		this.MAX = max;
		this.sospAtterraggi = 0;
		this.sospDecolli = 0;
		this.sospMezzi = 0;
		numMezzi = 0;
		this.occupato = false;
	}

	public void atterraggio() {
		lock.lock();
		try {
			while(occupato) {
				sospAtterraggi++;
				atterraggi.await();
				sospAtterraggi--;
			}
			occupato = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { lock.unlock(); }
	}

	public void decollo() {
		lock.lock();
		try {
			while(occupato || sospAtterraggi > 0) {
				sospDecolli++;
				decolli.await();
				sospDecolli--;
			}
			occupato = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { lock.unlock(); }
		
	}
	
	public void servizio() {
		lock.lock();
		try {
			while(occupato || sospAtterraggi > 0 || sospDecolli > 0 || numMezzi >= MAX) {
				sospMezzi++;
				servizio.await();
				sospMezzi--;
			}
			numMezzi++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void fineOperazioni(boolean mezzo) {
		lock.lock();
		try {
			if(mezzo)
				numMezzi--;
			else
				occupato = false;
			if(sospAtterraggi > 0)
				atterraggi.signalAll();
			else if(sospDecolli > 0)
				decolli.signalAll();
			else if(sospMezzi > 0)
				servizio.signalAll();
		} finally { lock.unlock(); }
	}

	public void printQueue() {
		System.out.println("Atterraggi in sospeso :" + sospAtterraggi + 
				" - Decolli in sospeso: " + sospDecolli + " - Mezzi in sospeso: " 
				+ sospMezzi + " - Mezzi in pista: " + numMezzi);;
	}
}


