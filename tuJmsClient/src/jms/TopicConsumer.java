package jms;

import java.io.Serializable;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
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
    
	private transient Queue requestsQueue;

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
		requestsQueue = session.createQueue("requests");
    }
    
	public void requestReply(String stockName) throws JMSException {

		TemporaryQueue replyQueue = session.createTemporaryQueue();
		MessageConsumer qConsumer = session.createConsumer(replyQueue);
		qConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message arg0) {
				onQuoteMessage(arg0);
			}
		});

		Message message = session.createMessage();
		message.setJMSReplyTo(replyQueue);
		message.setStringProperty("stockName", stockName);
		String correlationId = this.createRandomString();
		message.setJMSCorrelationID(correlationId);

		session.createProducer(requestsQueue).send(
				message);
	}

	private String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}

	private void onQuoteMessage(Message msg) {
		ObjectMessage message = (ObjectMessage) msg;
		StockQuote quote;
		try {
			quote = (StockQuote) message.getObject();
			tablePanel.updateTableObjects(quote);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

    public void subscribe(String stockName) throws JMSException{
    	
    	topic = session.createTopic("dax." + stockName);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener(){

			public void onMessage(Message arg0) {
				onQuoteMessage(arg0);
			}
        	
        });

		requestReply(stockName);
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
