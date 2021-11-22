import java.util.concurrent.*;

public class Trade extends Thread {
	
	Schedule.Task task;
	Semaphore semaphore;
	public Trade(Schedule.Task t, Semaphore sem) {
		this.task = t;
		this.semaphore = sem;
    }

	/**
	 * Trading function using locks
	 */
	public void run() {
		try {	
			semaphore.acquire();
			if(task.getQuantity() < 0)
				System.out.println(Utility.getZeroTimestamp() + " Starting sale of " + Math.abs(task.getQuantity()) + " stocks of " + task.getTradeCompany());
			else
				System.out.println(Utility.getZeroTimestamp() + " Starting purchase of " + task.getQuantity() + " stocks of " + task.getTradeCompany());
			
			Thread.sleep(1000);				
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}		
		if(task.getQuantity() < 0)
			System.out.println(Utility.getZeroTimestamp() + " Finishing sale of " + Math.abs(task.getQuantity()) + " stocks of " + task.getTradeCompany());
		else
			System.out.println(Utility.getZeroTimestamp() + " Finishing purchase of " + task.getQuantity() + " stocks of " + task.getTradeCompany());
		
		semaphore.release();

	}
}
