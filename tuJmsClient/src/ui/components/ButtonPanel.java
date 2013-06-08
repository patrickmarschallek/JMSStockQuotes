package ui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.main.MainFrame;
import util.Serializer;

public class ButtonPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton close, unsubscribe;
	private JTextField textField;
	private MainFrame mainFrame;
	private TablePanel tablePanel;
	
	public ButtonPanel(MainFrame mainFrame, TablePanel tablePanel){
		this.mainFrame = mainFrame;
		this.tablePanel = tablePanel;
		init();
	}

	public void init(){
		
		close = new JButton("Save & Close");
		close.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Serializer serializer = new Serializer(mainFrame.getStockMap(), "stockList.ser");
				serializer.writeObject();
				mainFrame.dispose();
			}

			
		});
		
		unsubscribe = new JButton("Unsubscribe");
		unsubscribe.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tablePanel.removeTableObject(textField.getText());		
			}
			
		});
		
		textField = new JTextField();
		
   	    setLayout(new BorderLayout(5,1));
    	add(close, BorderLayout.WEST);
    	add(textField, BorderLayout.CENTER);
      	add(unsubscribe, BorderLayout.EAST);
	}
}
