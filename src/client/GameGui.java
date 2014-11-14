package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

import objects.GameObject;
import util.Textures;

public class GameGui extends JPanel implements Runnable{

	//Window object
	private final JFrame window = new JFrame("2DMMO");
	
	//Height and width of the window
	private int width = 1280;
	private int height = 720;
	
	
	private boolean change = true;		//Map change signal
	private int posX = width/2;			//Character position on the screen
	private int posY = height/2;		//
	private int shiftX = 0;				//How much to shift the map in X plane
	private int shiftY = 0;				//How much to shift the map in Y plane
	private int buff = 100;				//Buffer on the side of the screen for drawing incoming objects
	
	
	//These are the map size variables
	//The actual pixel size is a multiple of 40
	private int boundsX = 100;
	private int boundsY = 100;
	
	ArrayList<GameObject> onScreenObjs;
	BufferedImage screen;
	BufferedImage map;
	
	public GameGui() {
		map = Textures.Map;
		screen = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		makeWindow();
		setKeyBindings();
	}
	
	private void makeWindow() {
		
		//Window close operation
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//For now the window will be a set size, later we will make it scalable
		this.setPreferredSize(new Dimension(width, height));
		
		//Make the window
		window.setContentPane(this);
		window.pack();
	}
	
	private void setKeyBindings() {
		this.getInputMap().put(KeyStroke.getKeyStroke('w'), "up");
		this.getInputMap().put(KeyStroke.getKeyStroke('a'), "left");
		this.getInputMap().put(KeyStroke.getKeyStroke('s'), "down");
		this.getInputMap().put(KeyStroke.getKeyStroke('d'), "right");
		
		Action playerControls = new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Action:" +shiftX + " " + shiftY + " " + arg0.getActionCommand());
				switch (arg0.getActionCommand()) {
				case "w":
					decShiftY();
					break;
				case "a":
					decShiftX();
					break;
				case "s":
					incShiftY();
					break;
				case "d":
					incShiftX();
					break;
				default:
					break;
				}
				
				if (shiftX < 0)
					shiftX = 0;
				if (shiftY < 0)
					shiftY = 0;
				
				changed();
				
			}
		};
		
		
		this.getActionMap().put("up", playerControls);
		this.getActionMap().put("left", playerControls);
		this.getActionMap().put("down", playerControls);
		this.getActionMap().put("right", playerControls);
		
	}
	
	@Override
	//This is the thread that will refresh the game display at 60fps
	public void run() {
		
		window.setVisible(true);
		Graphics2D g2 = (Graphics2D) screen.getGraphics();
		
		while (true) {
			if (change) {
				g2.drawImage(map.getSubimage(shiftX, shiftY, width, height), 0,0,width, height,null);
				drawObjects(g2);
				change = !change;
			}
			
			this.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void drawObjects(Graphics2D g2) {
		if (onScreenObjs != null) {
			for (GameObject G : onScreenObjs) {
				int X = (int)G.getLocation()[0];
				int Y = (int)G.getLocation()[1];
				
				if (X >= shiftX-buff && X <= shiftX+width+buff && Y >= shiftY-buff && Y <= shiftY+height+buff)
					g2.drawImage(G.getTexture(), X-shiftX, Y-shiftY, null);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(screen,0,0,width, height,null);
	}
	
	//When changing map elements, set this
	public void changed() {
		change = true;
	}
	
	public void refreshObjects(ArrayList<GameObject> N) {
		onScreenObjs = N;
		changed();
	}
	
	///////////Get methods//////////////
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getBoundsXY() {
		int[] tmp = new int[2];
		tmp[0] = boundsX;
		tmp[1] = boundsY;
		return tmp;
	}
	////////////////////////////////////
	
	/////Controls the map shifting//////
	public void incShiftX() {
		shiftX += 4;
	}
	
	public void incShiftY() {
		shiftY +=4;
	}

	public void decShiftX() {
		shiftX -= 4;
	}

	public void decShiftY() {
		shiftY -= 4;
	}

	////////////////////////////////////
	

}
