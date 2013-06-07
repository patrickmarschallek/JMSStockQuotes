package ui.components;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.JMSException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.StockQuote;

import jms.TopicConsumer;

public class SearchPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel infoLabel;
	private JTextField stockName;
	private JButton submitSearch;
	private TablePanel tablePanel;
		
	public SearchPanel(TablePanel tablePanel){
		this.tablePanel = tablePanel;
		init();
	}
      
	public void init(){
       
		infoLabel = new JLabel(" Enter a Stockname to subscribe:");
		infoLabel.setEnabled(true);
   	    
		stockName = new JTextField();
		stockName.setEditable(true);
		
		submitSearch = new JButton("Subscribe");
		submitSearch.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
					
					try {
						TopicConsumer consumer = new TopicConsumer(null, tablePanel);
						consumer.subscribe(getStockName().getText());
						StockQuote quote = new StockQuote();
						quote.setName(getStockName().getText());
						tablePanel.addQuote(consumer, quote);
					} catch (JMSException e) {
						JOptionPane.showMessageDialog(getRootPane(),
						"Something went wrong: \n" + e.getMessage(),
					    "Exception",
					    JOptionPane.ERROR_MESSAGE);
					}

					getStockName().setText("");				
		
			}
			
		});
       
   	    setLayout(new BorderLayout(5,1));
    	add(infoLabel, BorderLayout.WEST);
      	add(stockName, BorderLayout.CENTER);
      	add(submitSearch, BorderLayout.EAST);
    }
	
	public JTextField getStockName(){
		return stockName;
	}

	public JButton getSubmitSearch() {
		return submitSearch;
	}
}
