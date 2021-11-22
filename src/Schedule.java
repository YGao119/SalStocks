import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {

    /** 
     * Stock Trades Schedule 
     * Keep the track of tasks
    */
	
	private ArrayList<Task> tasks;
	private HashMap<Integer, ArrayList<Task>> tasksByStartTime;
	
	
    public Schedule() {
        tasks = new ArrayList<Task>(); 
        tasksByStartTime = new HashMap<Integer, ArrayList<Task>>();
    }
    //Insert a task to the schedule
    public void insertTask(Task t) {
    	tasks.add(t);
    }
    //remove a task to the schedule
    public Task removeTask() {
    	if(!tasks.isEmpty())
    		 return tasks.remove(0);
    	return null;
    }
    //check if schedule is empty
    public boolean isEmpty() {
    	return tasks.isEmpty();
    }
    
    public ArrayList<Task> getTasks(){
    	return this.tasks;
    }
    
    //sort schedule
    public HashMap<Integer, ArrayList<Task>> sortTasksByStartingTime(){
    	
    	for(Task t: tasks) {
    		if(tasksByStartTime.containsKey(t.getStartTime())) {
    			 ArrayList<Task> temp = tasksByStartTime.get(t.getStartTime());
    			 temp.add(t);  			 
    			 tasksByStartTime.put(t.getStartTime(), temp);
    		}
    		else {
    			 ArrayList<Task> temp = new ArrayList<Task>();
    			 temp.add(t);  	
    			 tasksByStartTime.put(t.getStartTime(), temp);		 
    		}
    	}
    	
    	return this.tasksByStartTime;
    }
    

    /**
     * Inner class to store task object
     */

    public static class Task {
    	private int start_time;
    	private String trade_company;
    	private int quant;
        public Task(int time, String ticker, int amount) {
        		this.start_time = time;
        		this.trade_company = ticker;
        		this.quant = amount;
        }
        
        public int getStartTime() {
        	return this.start_time;
        }
        public String getTradeCompany() {
        	return this.trade_company;
        }
        public int getQuantity() {
        	return this.quant;
        }
    }
    
}
