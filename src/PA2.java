import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;




public class PA2 {

	private static Schedule schedule;
	class Stocks{
		ArrayList<Stock> data;
	}
	private static Stocks stocks;
	private static HashSet<String> tickers = new HashSet<String>();
	private static HashMap<String, Semaphore> StockBrokers;
	private static Scanner dis = new Scanner(System.in);
     /**
      * Read Stock Json File inputed by user using GSON
      */
    private static void readStockFile() {
    	System.out.print("What is the name of the file containing the company information? ");
		String fileName = dis.nextLine();	
		Reader reader = null;
		Gson gson = new Gson();
		
		try {	
			if(fileName.equals(""))
				throw new IOException();
			reader = Files.newBufferedReader(Paths.get(fileName));
			stocks = gson.fromJson(reader, Stocks.class);
			if(stocks == null || stocks.data.size() == 0)
				throw new JsonParseException("Empty file");
			// validate Json file
			for(Stock s: stocks.data) {
				tickers.add(s.getTicker());
				s.validate();				
			}
			reader.close();
			System.out.println();
		}
		catch(IOException e) {
			System.out.println("The file " + fileName + " could not be found.");
			System.out.println();
			readStockFile();
		}		
		catch(JsonSyntaxException e) {
			System.out.println("Data cannot be converted to the proper type as shown above.");
			System.out.println();
			readStockFile();
		}
		catch(JsonParseException e) {
			System.out.println("Malformatting! " + e.getMessage());
			System.out.println();
			readStockFile();
		}

    }

    /**
     * Read Stock Trades CSV File inputed by user
     */
    private static void readScheduleFile() {
    	schedule = new Schedule();
    	System.out.print("What is the name of the file containing the schedule information? ");
		String fileName = dis.nextLine();
		String line = "";  
		String splitBy = ",";  
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))){	
			if(fileName.equals(""))
				throw new IOException();	
			while ((line = br.readLine()) != null)  { 
				String[] trade = line.split(splitBy);
				//check if each trade is valid
				if(trade.length < 3)
					throw new Exception("The file contains invalid trade that is missng parameter.");
				if(!tickers.contains(trade[1].trim()))
					throw new Exception("No such company! " + "Invalid trade: " +trade[1].trim() + " in the csv file.");
				if(Integer.parseInt(trade[0].trim()) < 0)
					throw new Exception("Invalid start time of trade " +trade[1].trim());
				
				schedule.insertTask(new Schedule.Task(Integer.parseInt(trade[0].trim()), trade[1].trim(), Integer.parseInt(trade[2].trim())));	
			}   
			br.close();
			System.out.println();
			
		}
		catch(IOException e) {
			System.out.println("The file " + fileName + " could not be found.");
			System.out.println();
			readScheduleFile();
		}	
		catch(NumberFormatException e) {
			System.out.println("The file contains invalid start time or trading quantity. Make sure it is provided as an integer.");
			System.out.println();
			readScheduleFile();
		}
		catch(Exception e) { 
			System.out.println(e.getMessage());
			System.out.println();
			readScheduleFile();
		}
		finally {
			dis.close();
		}
    	
    }

    /**
     *Set up Semaphore for Stock Brokers
     */

    private static void initializeSemaphor() {
   
    	StockBrokers = new HashMap<String, Semaphore>();
    	for(Stock s: stocks.data) {
    		StockBrokers.put(s.getTicker(), new Semaphore(s.getStockBrokers()));
    	}
    	
    }

    private static void executeTrades() throws InterruptedException {
    	System.out.println("Starting execution of program...");
    	HashMap<Integer, ArrayList<Schedule.Task>> sorted_schedule = schedule.sortTasksByStartingTime();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(schedule.getTasks().size());
        Utility.getZeroTimestamp();
    	while(!schedule.isEmpty()) {	
    		Schedule.Task temp = schedule.removeTask();		
    		ArrayList<Schedule.Task> s = sorted_schedule.get(temp.getStartTime());
     		for(Schedule.Task t: s) {
	    		Trade trade = new Trade(t, StockBrokers.get(t.getTradeCompany()));	
	    		scheduler.schedule(trade, t.getStartTime()* 1000,  TimeUnit.MILLISECONDS);		
    		}	
    		for(int i = 0; i < s.size()-1; i++) {
    			schedule.removeTask();		
    		}
    	}
    	scheduler.shutdown();
    	while (!scheduler.isTerminated()) {
    		Thread.yield();
    	}
    	System.out.println("All trades completed!");
    	
    }

    public static void main(String[] args) throws InterruptedException {	
    	readStockFile();
    	readScheduleFile();
    	initializeSemaphor();
    	executeTrades();
    }
}
