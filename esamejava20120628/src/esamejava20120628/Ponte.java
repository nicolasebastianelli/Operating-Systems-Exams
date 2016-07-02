package esamejava20120628;



import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ponte {
	private Lock lock = new ReentrantLock();
	private Condition oo = lock.newCondition();
	private Condition oe = lock.newCondition();
	private Condition eo = lock.newCondition();
	private Condition ee = lock.newCondition();
	private int sosp_oo = 0;
	private int sosp_oe = 0;
	private int sosp_eo = 0;
	private int sosp_ee = 0;
	private int occupatoOO = 0;
	private int occupatoEO = 0;
	private int occupatoOE = 0;
	private int occupatoEE = 0;
	private int maxTerrazza;
	private int nTerrazza = 0;

	public Ponte(int maxTerrazza) {
		super();
		this.maxTerrazza = maxTerrazza;
	}

	public void daOvestTrattoOInizio(int id) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (sosp_eo != 0 || maxTerrazza == nTerrazza || occupatoEO !=0) {
					System.out.println("[Visistatore " + id + "]: Sospeso (da Ovest tratto O)");
					sosp_oo++;
					oo.await();
					sosp_oo--;
				} else {
					fine = true;
					occupatoOO ++;
					System.out.println("[Visitatore " + id + "]: Entrato nel tratto O");
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void daOvestTrattoOFine(int id) throws InterruptedException {
		lock.lock();
		try {
			occupatoOO --;
			nTerrazza++;
			System.out.println("[Visitatore " + id + "]: Entrato nella terrazza (dal tratto O)");
			System.out.println("[Ponte]: Persone in terrazza: "+nTerrazza);
			if(sosp_eo != 0)
			{
				eo.signal();
			}
			else if(sosp_oo !=0)
			{
				oo.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	public void daOvestTrattoEInizio(int id) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (occupatoEE !=0) {
					System.out.println("[Visistatore " + id + "]: Sospeso (da Ovest tratto E)");
					sosp_oe++;
					oe.await();
					sosp_oe--;
				} else {
					fine = true;
					occupatoOE ++;
					nTerrazza--;
					System.out.println("[Visitatore " + id + "]: Uscito dalla terrazza (entrato nel tratto E) ");
					System.out.println("[Ponte]: Persone in terrazza: "+nTerrazza);
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void daOvestTrattoEFine(int id) throws InterruptedException {
		lock.lock();
		try {
			occupatoOE --;
			System.out.println("[Visitatore " + id + "]: Uscito dal tratto E");
			if(sosp_oe != 0)
			{
				oe.signal();
			}
			else if(sosp_ee !=0)
			{
				ee.signal();
			}
		} finally {
			lock.unlock();
		}
	}


	public void daEstTrattoOInizio(int id) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (occupatoOO!=0) {
					System.out.println("[Visistatore " + id + "]: Sospeso (da Est tratto O) ");
					sosp_eo++;
					eo.await();
					sosp_eo--;
				} else {
					fine = true;
					occupatoEO++;
					nTerrazza--;
					System.out.println("[Visitatore " + id + "]: Uscito dalla terrazza (entrato nel tratto O)");
					System.out.println("[Ponte]: Persone in terrazza: "+nTerrazza);
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void daEstTrattoOFine(int id) throws InterruptedException {
		lock.lock();
		try {
			occupatoEO --;
			System.out.println("[Visitatore " + id + "]: Uscito dal tratto O");
			if(sosp_eo != 0)
			{
				eo.signal();
			}
			else if(sosp_oo !=0)
			{
				oo.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	public void daEstTrattoEInizio(int id) throws InterruptedException {
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if (sosp_oe != 0 || maxTerrazza == nTerrazza || occupatoOE !=0) {
					System.out.println("[Visistatore " + id + "]: Sospeso (da Est tratto E)");
					sosp_ee++;
					ee.await();
					sosp_ee--;
				} else {
					fine = true;
					occupatoEE ++;
					System.out.println("[Visitatore " + id + "]: Entrato nel tratto E");
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void daEstTrattoEFine(int id) throws InterruptedException {
		lock.lock();
		try {
			occupatoEE --;
			nTerrazza++;
			System.out.println("[Visitatore " + id + "]: Entrato nella terrazza (dal tratto E)");
			System.out.println("[Ponte]: Persone in terrazza: "+nTerrazza);
			if(sosp_oe != 0)
			{
				oe.signal();
			}
			else if(sosp_ee !=0)
			{
				ee.signal();
			}
		} finally {
			lock.unlock();
		}
	}
	
}

