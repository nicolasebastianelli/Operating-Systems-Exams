package esamejava20120713;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cesto {
	private Lock lock = new ReentrantLock();
	private Condition d1 = lock.newCondition();
	private Condition d2 = lock.newCondition();
	private Condition s1 = lock.newCondition();
	private Condition s2 = lock.newCondition();
	private Condition operaio = lock.newCondition();
	private int sosp_d1 = 0;
	private int sosp_d2 = 0;
	private int sosp_s1 = 0;
	private int sosp_s2 = 0;
	private int maxCesto;
	private int nCestod1 = 0, nCestod2 = 0, nCestos1 = 0, nCestos2 = 0;
	private final static int DESTRO = 1;
	private final static int SINISTRO = 2;
	private final static int MISURA1 = 1;
	private final static int MISURA2 = 2;

	public Cesto(int maxCesto) {
		super();
		this.maxCesto = maxCesto;
	}

	public void inserisci(int piede, int misura,int i) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (piede == DESTRO && misura == MISURA1) {
					if (nCestod1 == (maxCesto/4)) {
						sosp_d1++;
						System.out.println("[Calzolaio"+i+"] sospeso");
						d1.await();
						sosp_d1--;
					} else {
						fine = true;
						nCestod1++;
						System.out.println("[Calzolaio "+i+"] Inserimento scarpa destra di misura 1 avvenuto");
						operaio.signalAll();
					}
				} else if (piede == DESTRO && misura == MISURA2) {
					if (nCestod2 == (maxCesto/4) || sosp_d1 != 0) {
						sosp_d2++;
						System.out.println("[Calzolaio "+i+"] sospeso");
						d2.await();
						sosp_d2--;
					} else {
						fine = true;
						nCestod2++;
						System.out.println("[Calzolaio "+i+"] Inserimento scarpa destra di misura 2 avvenuto");
						operaio.signalAll();
					}
				} else if (piede == SINISTRO && misura == MISURA1) {
					if (nCestos1 == (maxCesto/4) || sosp_d1 != 0 || sosp_d2 != 0) {
						sosp_s1++;
						System.out.println("[Calzolaio "+i+"] sospeso");
						s1.await();
						sosp_s1--;
					} else {
						fine = true;
						nCestos1++;
						System.out.println("[Calzolaio "+i+"] Inserimento scarpa sinistra di misura 1 avvenuto");
						operaio.signalAll();
					}
				} else if (piede == SINISTRO && misura == MISURA2) {
					if (nCestos2 == (maxCesto/4) || sosp_d1 != 0 || sosp_d2 != 0 || sosp_s1 != 0) {
						sosp_s2++;
						System.out.println("[Calzolaio "+i+"] sospeso");
						s2.await();
						sosp_s2--;
					} else {
						fine = true;
						nCestos2++;
						System.out.println("[Calzolaio "+i+"] Inserimento scarpa sinistra di misura 2 avvenuto");
						operaio.signalAll();
					}
				}

			}
			System.out.println("[Calzolaio "+i+"] nCestod1:"+nCestod1+" nCestod2:"+nCestod2+" nCestos1:"+nCestos1+" nCestos2:"+nCestos2);
		} finally {
			lock.unlock();
		}
	}

	public void inscatola() throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if ((nCestod1 == 0 || nCestos1 == 0)&&(nCestod2 == 0 || nCestos2 == 0)) {
					System.out.println("[Operaio] Sospeso");
					operaio.await();					
					System.out.println("[Operaio] Svegliato");
				}
				else{
				}
				fine=true;
				if ((nCestod1 != 0 && nCestos1 != 0)) {
					nCestod1--;
					nCestos1--;
					System.out.println("[Operaio] Coppia di scarpe di misura 1 inscatolata");
					System.out.println("[Operaio] nCestod1:"+nCestod1+" nCestod2:"+nCestod2+" nCestos1:"+nCestos1+" nCestos2:"+nCestos2);
					d1.signal();
					s1.signal();
					
				}
				else if(nCestod2 != 0 && nCestos2 != 0){
					nCestod2--;
					nCestos2--;
					System.out.println("[Operaio]Coppia di scarpe di misura 2 inscatolata");
					System.out.println("[Operaio] nCestod1:"+nCestod1+" nCestod2:"+nCestod2+" nCestos1:"+nCestos1+" nCestos2:"+nCestos2);
					d2.signal();
					s2.signal();
				}
			}
		} finally {
			lock.unlock();
		}
	}

}
