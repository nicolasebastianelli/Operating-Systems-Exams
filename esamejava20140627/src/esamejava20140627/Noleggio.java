package esamejava20140627;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Noleggio {
	private Lock lock = new ReentrantLock();
	private Condition meno_p = lock.newCondition();
	private Condition piu_p = lock.newCondition();
	private Condition meno_r = lock.newCondition();
	private Condition piu_r = lock.newCondition();
	private int sosp_meno_p = 0;
	private int sosp_piu_p = 0;
	private int sosp_meno_r = 0;
	private int sosp_piu_r = 0;
	private int cassa = 0;
	private int banca = 0;
	private int n_pattini;
	private Boolean assente = false;

	public Noleggio(int n) {
		super();
		n_pattini = n;
	}

	public void prelievo(int persone,int idg) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (persone <= 4) {
					if (n_pattini == 0 || assente == true) {
						sosp_meno_p++;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone in uscita sospeso");
						meno_p.await();
						sosp_meno_p--;

					} else {
						fine = true;						
						cassa = cassa + 4 + 3;
						n_pattini--;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone ha affittato pattino, pattini disponibili:"+n_pattini+", cassa:"+cassa);
					}
				} else {
					if (n_pattini < 2 || assente == true || sosp_meno_p != 0) {
						sosp_piu_p++;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone in uscita sospeso");
						piu_p.await();
						sosp_piu_p--;
					} else {
						fine = true;	
						cassa = cassa + 4 + 3 + 4 + 3;
						n_pattini = n_pattini - 2;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone ha affittato pattino, pattini disponibili:"+n_pattini+", cassa:"+cassa);
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void restituzione(int persone,int idg) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (persone <= 4) {
					if (assente == true || sosp_piu_r != 0|| cassa< 3) {
						sosp_meno_r++;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone in rientro sospeso");
						meno_r.await();
						sosp_meno_r--;

					} else {
						fine = true;
						cassa = cassa - 3;
						n_pattini++;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone ha restituito pattino, pattini disponibili:"+n_pattini+", cassa:"+cassa);
						if (sosp_meno_p != 0) {
							meno_p.signal();
						} else if (sosp_piu_p != 0) {
							piu_p.signal();
						}
						if (sosp_piu_r == 0) {
							meno_r.signal();
						}
					}
				} else {
					if (assente == true|| cassa<6) {
						sosp_piu_r++;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone in rientro sospeso");
						piu_r.await();
						sosp_piu_r--;
					} else {
						fine = true;
						cassa = cassa - 3 - 3;
						n_pattini = n_pattini + 2;
						System.out.println("Gruppo ["+idg+"] di " + persone + " persone ha restituito pattino, pattini disponibili:"+n_pattini+", cassa:"+cassa);
						if (sosp_meno_p != 0) {
							meno_p.signal();
							meno_p.signal();
						} else if (sosp_piu_p != 0) {
							piu_p.signal();
							piu_p.signal();
						}
						if (sosp_piu_r != 0) {
							piu_r.signal();
						} else if (sosp_meno_r != 0) {
							meno_r.signal();
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void chiusura() {
		lock.lock();
		try {
			assente = true;
			System.out.println("Noleggio chiuso, La cassa contiene:" + cassa + " euro e la banca ha:" + banca + " euro");
			if (cassa > 3) {
				banca = cassa - 3;
				cassa = 3;
			}
		} finally {
			lock.unlock();
		}
	}

	public void apertura() {
		lock.lock();
		try {
			assente = false;
			System.out.println("Noleggio riaperto");
			if (sosp_meno_p != 0) {
				meno_p.signalAll();
			} else if (sosp_piu_p != 0) {
				piu_p.signalAll();
			}
			if (sosp_piu_r != 0) {
				piu_r.signalAll();
			} else if (sosp_meno_r != 0) {
				meno_r.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

}
