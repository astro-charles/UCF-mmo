package client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class ChatPannel extends JPanel{
	private static String[] log;
	private final static JLabel text = new JLabel();
	private final static JTextField box = new JTextField();
	private final static Container c = new Container();
	int height = 150;
	int width = 300;
	
	public ChatPannel() {

		box.enableInputMethods(false);
		
		log = new String[8];

		text.setText(makeText());
		text.setAlignmentX(SwingConstants.LEFT);
		this.setBounds(0, 0, width, height);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		box.setMaximumSize(new Dimension(width,25));
		
		
		c.setLayout(new BoxLayout(c,BoxLayout.LINE_AXIS));
		c.setMaximumSize(new Dimension(1000,30));
		c.add(box);
		
		
		JButton enter = new JButton("Enter");
		enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ServerComm.writeMessage(box.getText());
				box.setText(null);
				
			}
			
		});
		
		JButton hide = new JButton("Hide");
		hide.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				GameGui.resetKeyListener();
			}
			
		});
		
		c.add(enter);
		c.add(hide);
		
		this.add(text);
		this.add(Box.createVerticalGlue());
		this.add(c);
		
			
		this.setVisible(false);
		
		
	}
	public void addText(char c) {
		box.setText(box.getText()+c);
	}
	
	public void toggleVisible() {
		super.setVisible(!super.isVisible());
	}
	
	public static void makeVisible() {
		c.setVisible(true);
	}
	
	public static void addString(String s) {
		for (int i=1; i<log.length; i++) {
			log[i-1] = log[i];
		}
		
		log[log.length -1] = s;
		text.setText(makeText());
		
	}
	
	private static String makeText(){
		String out = "<html>";
		for (String s : log){
			if (s != null)
				out += s+"<br>";
		}
		out += "</html>";
		return out;
	}
}
