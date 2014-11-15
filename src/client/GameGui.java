package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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

public class GameGui extends JPanel implements Runnable, KeyListener, ActionListener{

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
	private int buff = 100;				//Amount of pixels off screen on the boarders, used for drawing incoming objects
	
	private boolean[] keyHolds = new boolean[4];	//Tracks which keys are being presses 0=W, 1=A, 2=S, 3=D
	private int characterDirection = 0; 			//0-7, the eight possible ways for them to face
	
	//These are the map size variables
	//The actual pixel size is a multiple of 40
	private int boundsX = 100*40;
	private int boundsY = 100*40;
	
	ArrayList<GameObject> onScreenObjs;
	BufferedImage screen;
	BufferedImage map;
	
	public GameGui() {
		
		//Gets the map image
		map = Textures.Map;
		
		//Buffered image to draw to
		screen = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		//Sets window defaults
		makeWindow();
		
		//setKeyBindings();
	}
	
	private void makeWindow() {
		
		//Window close operation
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//For now the window will be a set size, later we will make it scalable
		this.setPreferredSize(new Dimension(width, height));
		
		//Key listener for character control
		window.addKeyListener(this);
		
		//Make the window
		window.setContentPane(this);
		window.pack();
		
		window.setVisible(true);
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
		
		//Sets the window to visible when the program starts
		window.setVisible(true);
		
		//Graphics object for the image we are going to draw to
		Graphics2D g2 = (Graphics2D) screen.getGraphics();
		
		
		//Thread loop for the gui
		while (true) {
			
			/*Handles the movement of the map based on key presses*/
			if (keyHolds[0]) {
				decShiftY();
				change = true;
			}
			else if (keyHolds[2]){
				incShiftY();
				change = true;
			}
			
			if (keyHolds[1]) {
				decShiftX();
				change = true;
			}
			else if (keyHolds[3]) {
				incShiftX();
				change = true;
			}
				
			checkBounds();
			
			//If there is change on the map, draw it
			if (change) {
				//Gets the current position and draws that subimage to screen
				g2.drawImage(map.getSubimage(shiftX, shiftY, width, height), 0,0,width, height,null);
				
				//Draws in game objects
				drawObjects(g2);
				
				//Resets change variable
				change = !change;
			}
			
			
			
			//repaint screen
			this.repaint();
			
			//Sleep for every 10 milliseconds
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	//Alternative to run
	@Override
	public void actionPerformed(ActionEvent arg0) {
					
		/*Handles the movement of the map based on key presses*/
		if (keyHolds[0]) {
			decShiftY();
			change = true;
		}
		else if (keyHolds[2]){
			incShiftY();
			change = true;
		}
		
		if (keyHolds[1]) {
			decShiftX();
			change = true;
		}
		else if (keyHolds[3]) {
			incShiftX();
			change = true;
		}
				
		//If there is a change in the map
		if (change) {
			//Check if we are in the map bounds
			checkBounds();
			
			//Repaints the change
			this.repaint();
		}
		
	}
	
	
	@Override
	public void keyPressed(java.awt.event.KeyEvent arg0) {
			switch (arg0.getKeyChar()) {
			case 'w':
				if (keyHolds[2])
					keyHolds[2] = false;
				
				keyHolds[0] = true;
				break;
			case 'a':
				if (keyHolds[3])
					keyHolds[3] = false;
				
				keyHolds[1] = true;
				break;
			case 's':
				if (keyHolds[0])
					keyHolds[0] = false;
				
				keyHolds[2] = true;
				break;
			case 'd':
				if (keyHolds[1])
					keyHolds[1] = false;
				
				keyHolds[3] = true;
				break;
			default:
				System.out.println("Invalid key press.");
				break;
			}
		
	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case 'w':
			keyHolds[0] = false;
			break;
		case 'a':
			keyHolds[1] = false;
			break;
		case 's':
			keyHolds[2] = false;
			break;
		case 'd':
			keyHolds[3] = false;
			break;
		default:
			System.out.println("Invalid key press.");
			break;
		}
		
	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent arg0) {
		System.out.println("Pressed " + arg0.getKeyChar());
		
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
		
		//If there is change on the map, draw it
		//Gets the current position and draws that subimage to screen
		g2.drawImage(map.getSubimage(shiftX, shiftY, width, height), 0,0,width, height,null);
		
		//Draws in game objects
		drawObjects(g2);	
	}
	
	//When changing map elements, set this
	public void changed() {
		change = true;
	}
	
	public void refreshObjects(ArrayList<GameObject> N) {
		onScreenObjs = N;
		changed();
	}
	
	//Checks if the shift variable is within acceptable limits,
	//resets them if they aren't
	private void checkBounds() {
		if (shiftX > boundsX-width)
			shiftX = boundsX-width;
		if (shiftX < 0)
			shiftX = 0;
		if (shiftY > boundsY-height)
			shiftY = boundsY-height;
		if (shiftY < 0)
			shiftY = 0;
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
	
	private int pixChange = 3;
	
	public void incShiftX() {
		shiftX += pixChange;
	}
	
	public void incShiftY() {
		shiftY += pixChange;
	}

	public void decShiftX() {
		shiftX -= pixChange;
	}

	public void decShiftY() {
		shiftY -= pixChange;
	}

	

	////////////////////////////////////
	

}
