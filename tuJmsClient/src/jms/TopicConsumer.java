package jms;

import java.io.Serializable;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import ui.components.TablePanel;
import util.StockQuote;

public class TopicConsumer implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient ActiveMQConnectionFactory factory ;
	private transient Connection connection;
    private transient Session session;
    private String url;
    private Topic topic;
    private transient TablePanel tablePanel;
    
    public TopicConsumer(String url,TablePanel tablePanel) throws JMSException{ 
        if(url == null){
        	this.url = "tcp://localhost:61616";
        }else{
        	this.url = url;
        }
        
        this.tablePanel = tablePanel;
        init();

    }
    
    public void init() throws JMSException{
    	factory = new ActiveMQConnectionFactory(url);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
    }
    
    public void subscribe(String stockName) throws JMSException{
    	
    	topic = session.createTopic("dax." + stockName);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener(){

			public void onMessage(Message arg0) {
				ObjectMessage message = (ObjectMessage) arg0;
				StockQuote quote;
				try {
					quote = (StockQuote) message.getObject();
					tablePanel.updateTableObjects(quote);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
        	
        });
        
        connection.start();
    }
    
    public void unsubscribe() throws JMSException {
    	connection.close();
    }

	public Connection getConnection() {
		return connection;
	}

	public TablePanel getTablePanel() {
		return tablePanel;
	}

	public void setTablePanel(TablePanel tablePanel) {
		this.tablePanel = tablePanel;
	}

}
