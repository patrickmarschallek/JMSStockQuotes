package ui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Topic;
import javax.swing.JFrame;

import jms.TopicConsumer;

import ui.components.ButtonPanel;
import ui.components.SearchPanel;
import ui.components.TablePanel;
import util.Serializer;
import util.StockQuote;
public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,Map<StockQuote, TopicConsumer>> stockMap = new HashMap<String, Map<StockQuote, TopicConsumer>>();
	
	public MainFrame(){
		super("JMS Stock Client");
		init();
	}
	
	private void init(){
		
		//Gesamt Box Einstellung
		TablePanel tablePanel = new TablePanel(this);
		tablePanel.setPreferredSize(new Dimension(800,100));
		
		this.add(tablePanel, BorderLayout.CENTER);
	
		Serializer serializer = new Serializer("stockList.ser");
		stockMap = serializer.readObject(tablePanel);
		
		tablePanel.refreshTableUI();
		
		//Gesamt Box Einstellung
		SearchPanel searchPanel = new SearchPanel(tablePanel);
		searchPanel.setPreferredSize(new Dimension(800,30));
		
		this.add(searchPanel, BorderLayout.PAGE_START);
		
		//Gesamt Box Einstellung
		ButtonPanel buttonPanel = new ButtonPanel(this, tablePanel);
		buttonPanel.setPreferredSize(new Dimension(800,30));
		
		this.add(buttonPanel, BorderLayout.PAGE_END);
		
		//MainFrame Einstellungen
		//this.setBackground(Color.BLUE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(50, 50);
		this.pack();
		this.setVisible(true);
		this.setFocusable(true);
	}

	public Map<String, Map<StockQuote, TopicConsumer>> getStockMap() {
		return stockMap;
	}

	public void setStockMap(Map<String, Map<StockQuote, TopicConsumer>> stockMap) {
		this.stockMap = stockMap;
	}	
	
}
