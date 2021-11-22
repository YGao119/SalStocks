import com.google.gson.JsonParseException;

public class Stock {
    /**
	 * Here: all the needed class members and their getters and setters
	 */
	private String name;
	private String ticker;
	private String startDate;
	private Integer stockBrokers;
	private String description;
	private String exchangeCode;
	
    public Stock() {

    }
    
    public String getTicker() {
    	return this.ticker;
    }
    
    public int getStockBrokers() {
    	return this.stockBrokers;
    }
    
    //validate task
    public void validate() {  
    	if(name == null || ticker == null || startDate == null || stockBrokers == null || description == null|| exchangeCode == null)
    		throw new JsonParseException("Missing data parameters");
    	if(stockBrokers <= 0)
    		throw new JsonParseException("Invalid number of stockBrokers");	
    }



}

