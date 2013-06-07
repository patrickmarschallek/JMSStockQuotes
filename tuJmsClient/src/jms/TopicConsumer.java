package jms;

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

public class TopicConsumer {
	
    private Connection connection;
    private Session session;
    private String url;
    private Topic topic;
    private TablePanel tablePanel;
    
    public TopicConsumer(String url,TablePanel tablePanel) throws JMSException{ 
        if(url == null){
        	this.url = "tcp://localhost:61616";
        }else{
        	this.url = url;
        }
        
        this.tablePanel = tablePanel;
        init();

    }
    
    private void init() throws JMSException{
    	
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
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

}
