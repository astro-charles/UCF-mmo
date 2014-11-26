package client;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import objects.GameObject;
import objects.MapElements;
import objects.Mobs;
import util.MathUtil;
import util.Textures;

/*
 * The Client class contains all relevant data for the client and 
 * is in charge of updating the screen, storing game objects, and
 * communicating with the server. It will also handle keyboard 
 * events (use key binding not key listener)
 */

public class Client implements Runnable{
	private ArrayList<GameObject> allMapObjects;
	private GameGui GUI;
	private ServerComm serv;
    private Audio soundplayer;
    public static boolean sendMovement = false;
    private String charName;
    
	public Client() {
		//getLogin();
		startGame();
	}
	
	
	@Override
	//Updates client every second
        public void run() {
            ObjectOutputStream out = serv.getOutput();
            //ObjectInputStream in = serv.getInput();

            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //if (GUI.getChange())
                try {
                    
                	if (sendMovement) {
                		out.writeObject(GUI.getReleventData());
                		out.flush();
                		sendMovement = false;
                	}
                	
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("\nLost Connection to the server...");
                    break;
                }
            }

        }
	
	private void startGame() {
		//soundplayer = new Audio();
        //soundplayer.begin();
        //server.DBConnector db = new server.DBConnector();
        //server.DBConnector.createConnection(null, null, null, null)
		int mapX = 100*40;
		int mapY = mapX;
		
		Textures.doAll();
		
		ArrayList<MapElements> E = MathUtil.genMap(mapX, mapY);
		ArrayList<Mobs> M = new ArrayList<Mobs>();
         
                
        DrawMoving mov = new DrawMoving(M,E);
		
        GUI = new GameGui(mov, mapX, mapY);
                
        serv = new ServerComm(M, GUI.getPosition()[0], GUI.getPosition()[1], charName);
		
		if (!serv.connected) {
                    System.out.println("Failed to connect.");
                    System.exit(-1);;
		}
			
		Thread s = new Thread(serv);
                s.start();
		  
		Timer t = new Timer(20, GUI);
		t.start();
		
		this.run();
	}
	
	private void getLogin() {
		final JFrame startScreen = new JFrame("UCF MMO");
		//startScreen.setPreferredSize(new Dimension(200,200));
		Container con = new Container();
		con.setLayout(new BoxLayout(con, BoxLayout.PAGE_AXIS));
		
		JLabel text = new JLabel ("Enter username below.");
		text.setAlignmentX(SwingConstants.LEFT);
		
		Container footer = new Container();
		footer.setLayout(new BoxLayout(footer, BoxLayout.LINE_AXIS));
		
		final JTextField entry = new JTextField();
		
		
		JButton ok = new JButton("Enter");
		
		ActionListener buttonPress = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = entry.getText();
				
				if (s != "") {
					charName = s;
					startGame();
					startScreen.dispose();
				}
				
			}
			
		};
		
		ok.addActionListener(buttonPress);;
		
		footer.add(entry);
		footer.add(ok);
		
		con.add(text);
		con.add(footer);
		
		startScreen.setContentPane(con);
		startScreen.pack();
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		
		Thread client = new Thread(new Client());
	}

}
