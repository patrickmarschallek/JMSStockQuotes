package ui.components;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import jms.TopicConsumer;

import ui.main.MainFrame;
import util.StockQuote;

public class TablePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable stockTable;
	private MainFrame mainFrame;
	
	//Simple column and name arrays
	String[] columnNames = {"ID", "Stock-Name", "Price", "Time"};
	
	Object[][] data = {
			{"", "" , "", ""},
			{"", "" , "", ""},
			{"", "" , "", ""},
			{"", "" , "", ""},
			{"", "" , "", ""},
			{"", "" , "", ""}
	};
	
	public TablePanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		init();
	}

	private void init(){
		resetArray();
		stockTable = new JTable(data, columnNames);
		stockTable.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(stockTable);
		stockTable.setFillsViewportHeight(true);
		
   	    setLayout(new BorderLayout(1,1));
   	    add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addQuote(TopicConsumer consumer, StockQuote quote){
		boolean contains = false;
		
		for(String stockName:mainFrame.getStockMap().keySet()){
			if(stockName.equals(quote.getName()))
				contains = true;
		}
		
		if(contains){
			JOptionPane.showMessageDialog(getRootPane(),
			"You are allready subscribed to this topic",
		    "Exception",
		    JOptionPane.ERROR_MESSAGE);
		}else{
			Map<StockQuote, TopicConsumer> tempMap = new HashMap<StockQuote,TopicConsumer>();
			tempMap.put(quote, consumer);
			mainFrame.getStockMap().put(quote.getName(), tempMap);
		}
	}
	
	public void updateTableObjects(StockQuote quote){
		Map<StockQuote, TopicConsumer> oldMap = new HashMap<StockQuote,TopicConsumer>();
		//Update stockMap to hold the new Stock object received in the message
		oldMap = mainFrame.getStockMap().get(quote.getName());
		mainFrame.getStockMap().remove(quote.getName());
		
		for(Map.Entry<StockQuote, TopicConsumer> it:oldMap.entrySet()){
			StockQuote sQuote = it.getKey();
			TopicConsumer cons = it.getValue();
			
			if(sQuote.getName().equals(quote.getName())){
				Map<StockQuote, TopicConsumer> newMap = new HashMap<StockQuote,TopicConsumer>();
				newMap.put(quote, cons);
				mainFrame.getStockMap().put(quote.getName(), newMap);
			}
		}
		
		refreshTableUI();
	}
	
	public void refreshTableUI(){
		int i = 0;
		resetArray();
		for(Map.Entry<String, Map<StockQuote, TopicConsumer>> it:mainFrame.getStockMap().entrySet()){
			Map<StockQuote, TopicConsumer> tempMap = new HashMap<StockQuote,TopicConsumer>();
			tempMap = it.getValue();
			for(StockQuote stockQuote:tempMap.keySet()){
				Object[] row = { stockQuote.getIsin(), stockQuote.getName() , stockQuote.getQuote(), new Date(stockQuote.getTimeInMillis())};
				data[i] = row;
				i++;
			}
		}
		


		
		stockTable = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(stockTable);
		stockTable.setFillsViewportHeight(true);
		
   	    setLayout(new BorderLayout(1,1));
   	    this.add(scrollPane, BorderLayout.CENTER);
   		this.updateUI();
	}
	
	public void removeTableObject(String name){
		Map<StockQuote, TopicConsumer> tempMap = new HashMap<StockQuote,TopicConsumer>();
		TopicConsumer consumer;
		tempMap = mainFrame.getStockMap().get(name);
		if(tempMap != null){
		for(Map.Entry<StockQuote, TopicConsumer> it:tempMap.entrySet()){
			 consumer = it.getValue();
			 try {
				consumer.getConnection().close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		mainFrame.getStockMap().remove(name);
		refreshTableUI();
		}else{
			JOptionPane.showMessageDialog(getRootPane(),
			"Please enter a valid stock name!",
		    "Exception",
		    JOptionPane.ERROR_MESSAGE);
		}
	}
		
	private void resetArray(){
		for(int i = 0; i<=5; i++){
			Object[] row = {"", "" , "", ""};
			data[i] = row;
		}
	}
	
	
}
