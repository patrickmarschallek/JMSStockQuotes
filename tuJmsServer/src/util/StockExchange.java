package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jms.TopicPublisher;

public class StockExchange {
	
	private static TopicPublisher publisher;
	private static int counter = 1;
	public StockExchange(){
		this.publisher = new TopicPublisher(null);
	}
	
	public void run(){
		Timer timer = new Timer();
		ExchangeTask exTask = new ExchangeTask();
		timer.scheduleAtFixedRate(exTask, new Date(System.currentTimeMillis()), 15000);
	}
	
	public void close(){
		publisher.close();
	}
	
    private static ArrayList<StockQuote> generateQuotes(){
    	ArrayList<StockQuote> quoteList = new ArrayList<StockQuote>();
    	
    	StockQuote adidas = new StockQuote("DE00A1EWWW0", "Adidas", 80.92, System.currentTimeMillis());
    	StockQuote basf = new StockQuote("DE000BASF111", "BASF", 71.45, System.currentTimeMillis());
    	StockQuote dtTelekom = new StockQuote("DE0005557508", "deutsche Telekom", 8.68, System.currentTimeMillis());
    	StockQuote eon = new StockQuote("DE000ENAG999", "Eon", 12.70, System.currentTimeMillis());
    	StockQuote vw = new StockQuote("DE0007664039", "VW", 161.50, System.currentTimeMillis());
    	
    	quoteList.add(adidas);
    	quoteList.add(basf);
    	quoteList.add(dtTelekom);
    	quoteList.add(eon);
    	quoteList.add(vw);
    	
    	return quoteList;
    }
    
    private static class ExchangeTask extends TimerTask{

		@Override
		public void run() {
			for(StockQuote quote: generateQuotes()){
				double newStockQuote = 0.00;
				if(counter == 1){
					newStockQuote = quote.getQuote() + (quote.getQuote() * 0.002);
					counter = 0;
				}else if(counter == 0){
					newStockQuote = quote.getQuote() - (quote.getQuote() * 0.002);
					counter = 1;
				}
				quote.setQuote(newStockQuote);
				publisher.publishObjectMessage(quote);
			}
			
		}
    	
    }

}
