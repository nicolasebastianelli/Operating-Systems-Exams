package esercizio11;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Date;
import java.util.concurrent.locks.Condition;

public class pista {
	private Lock lock = new ReentrantLock();
	private Condition A = lock.newCondition();
	private Condition D = lock.newCondition();
	private Condition S = lock.newCondition();
	private int sospD = 0;
	private int sospS = 0;
	private int sospA = 0;
	private int max;
	private int cont = 0;
	private Date d=new Date();
	private Boolean occupato = false;

	public pista(int Max) {
		super();
		this.max = Max;
	}

	public void atterraggio(int n) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (occupato == true) {
					sospA++;
					A.await();
				} else {
					occupato = true;
					fine = true;
				}
			}
			lock.unlock();
			Thread.sleep(1000);
			lock.lock();
			if (sospA == 0 && sospD != 0) {
				D.signal();
				sospD--;
			} else if (sospD == 0 && sospS != 0) {
				S.signalAll();
				sospS = 0;
			} else if(sospA!=0){
				A.signal();
				sospA--;
			}
			occupato=false;
			System.out.println("atterraggio " + n + " "+d.toString());
		} finally {
			lock.unlock();
		}
	}

	public void decollo(int n) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (occupato == true || sospA != 0) {
					sospD++;
					D.await();
				} else {
					occupato = true;
					fine = true;
				}
			}
			lock.unlock();
			Thread.sleep(1000);
			lock.lock();
			if (sospA == 0 && sospD != 0) {
				D.signal();
				sospD--;
			} else if (sospD == 0 && sospS != 0) {
				S.signalAll();
				sospS = 0;
			} else if(sospA!=0){
				A.signal();
				sospA--;
			}
			occupato = false;
			System.out.println("decollo " + n + " "+d.toString());
		} finally {
			lock.unlock();
		}
	}

	public void servizi(int n) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (sospD != 0 || sospA != 0 || cont == max || (occupato == true && cont == 0)) {
					sospS++;
					S.await();
				} else {
					fine = true;
					occupato = true;
				}
			}
			cont++;
			lock.unlock();
			Thread.sleep(1000);
			lock.lock();
			cont--;
			if (sospA == 0 && sospD != 0) {
				D.signal();
				sospD--;
			} else if (sospD == 0 && sospS != 0) {
				S.signal();
				sospS --;
			} else if(sospA!=0){
				A.signal();
				sospA--;
			}
			if(cont==0)occupato= false;
			
		} finally {
			lock.unlock();
		}
	}

}
