package client;

import java.awt.Color;
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

import objects.GameObject;
import objects.MapElements;
import objects.Mobs;
import util.Textures;

public class GameGui extends JPanel implements KeyListener, ActionListener{

	//Window object
	private final JFrame window = new JFrame("2DMMO");
	
	//Height and width of the window
	private int width = 1280;
	private int height = 720;
	
	
	private boolean change = true;		//Map change signal
	private int posX = width/2;			//Character position on the screen
	private int posY = height/2;		//
	private int shiftX = 0;				//How much to shift the map in X plane
	private int shiftY = 300;			//How much to shift the map in Y plane
	
	
	//Variables used to check collision before changing the actual position
	private int cposX = 0;			
	private int cposY =  0;		
	private int cshiftX = 0;				
	private int cshiftY = 0;				
	
	private int buff = 100;							//Amount of pixels off screen on the boarders, used for drawing incoming objects
	
	private boolean[] keyHolds = new boolean[4];	//Tracks which keys are being presses 0=W, 1=A, 2=S, 3=D
	private int characterDirection = 1; 			//0-7, the eight possible ways for them to face
	private int characterStep = 3;					//Current step position the character is on
	private final int walkSpeed = 4;				//Speed the character walk animation plays, not speed across map
	private int walkTimer;							//Timer to help control speed
	
	private int maxHealth;
	private int charXP;
	private int charHealth;
	private int charCoins;
	private int charLvl;
	private final int hitCons = 5;
	
	private int attackTimer  = 0;
	//private final int attackSpeed = 10;
	private boolean attacking = false;
	
	//Map size in pixels
	private int boundsX = 100*40;
	private int boundsY = 100*40;
	
	//ArrayList<MapElements> onScreenObjs;
	DrawMoving movers;
	BufferedImage map;
	BufferedImage character;

	
	public GameGui(DrawMoving d, int bonX, int bonY) {
		movers = d;
		boundsX = bonX;
		boundsY = bonY;
		
		//Gets the map image
		map = Textures.Map;
		character = Textures.vlad[0][characterDirection][characterStep];
		
		maxHealth = 100;
		charHealth = maxHealth;
		charCoins = 10;
		charXP = 0;
		charLvl = 1;
		
		//Sets window defaults
		makeWindow();
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
	
	/*
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
	
	/*
	@Override
	//This is the thread that will refresh the game display at 60fps
	public void run() {
		
		//Sets the window to visible when the program starts
		window.setVisible(true);
		
		//Graphics object for the image we are going to draw to
		Graphics2D g2 = (Graphics2D) screen.getGraphics();
		
		
		//Thread loop for the gui
		while (true) {
			
			/*Handles the movement of the map based on key presses
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
	*/
	
	//Refreshes the screen with new content
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (ServerComm.mobChange)
			repaint();
		
		cposX = 0;			
		cposY =  0;		
		cshiftX = 0;				
		cshiftY = 0;		
			
		slideMap();
				
		//If there is a change in the map
		if (change || attacking) {
			
			Client.sendMovement = true;
			
			Rectangle r = new Rectangle(
					posX + shiftX + cposX + cshiftX + (character.getWidth()-30)/2 -character.getWidth()/2,
					posY + shiftY + cposY + cshiftY +(character.getHeight()-70)/2 -character.getHeight()/2 + 40,
					30,
					30);
	
			if (movers.detectCollision(r)) {
				cposX = 0;			
				cposY =  0;		
				cshiftX = 0;				
				cshiftY = 0;
			}
			
			posX += cposX;			
			posY +=  cposY;		
			shiftX += cshiftX;				
			shiftY += cshiftY;
			
			if (attacking)
				updateCharacterAttack();
			else
				updateCharacterWalking();
			
			//Check if we are in the map bounds
			checkShift();
			
			//Repaints the change
			this.repaint();
			change = false;
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
			case ' ':
				attacking = true;
				attackTimer = 0;
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
                case ' ':break;
		default:
			System.out.println("Invalid key press.");
			break;
		}
		
	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent arg0) {
		//System.out.println("Pressed " + arg0.getKeyChar());
		
	}
	
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//If there is change on the map, draw it
		//Gets the current position and draws that subimage to screen
		g2.drawImage(map.getSubimage(shiftX, shiftY, width, height), 0,0,width, height,null);
		
		//Draws in game objects
		movers.drawObjects(g2, shiftX, shiftY, width, height);
		movers.drawMobs(g2, shiftX, shiftY, width, height);
		
		g2.setColor(Color.PINK);
		g2.drawImage(character, posX-character.getWidth()/2, posY-character.getHeight()/2, null);
		
		
		g2.drawRect(
				posX + cposX -character.getWidth()/2 + (character.getWidth()-30)/2,
				posY + cposY -character.getHeight()/2 + (character.getHeight()-70)/2 + 40,
				30,
				30);
		
		
	}
	
	//When changing map elements, set this
	public void changed() {
		change = true;
	}
	
	private void slideMap() {
		/*Handles the movement of the map based on key presses*/
		//Up
		
		if (keyHolds[0]) {
			
			if (shiftY == 0 || shiftY == boundsY-height)	//If at edge of map, change character position
				decPosY();
			else
				decShiftY();	//Else, shift map
			
			change = true;
		}
		//Down
		else if (keyHolds[2]){
			
			if (posY < height/2 || shiftY == boundsY-height)	//If at edge of map, increment until centered
				incPosY();
			else
				incShiftY();
			
			change = true;
		}
		
		//Left
		if (keyHolds[1]) {
			
			if (shiftX == 0 || shiftX == boundsX - width)	//If at edge of map, change character position
				decPosX();
			else
				decShiftX();	//Else, shift map
			
			change = true;
		}
		//Right
		else if (keyHolds[3]) {
			
			if (posX < width/2 || shiftX == boundsX - width)		//If at edge of map, increment until centered
				incPosX();
			else
				incShiftX();
			change = true;
		}
	}
	
	
	private void updateCharacterWalking() {
		
		if (walkTimer++ > walkSpeed) {
			
			walkTimer = 0;
			characterStep += 1;
			if (characterStep > 7)
				characterStep = 0;
		
			if (keyHolds[0])
				characterDirection = 1;
			if (keyHolds[1])
				characterDirection = 7;
			if (keyHolds[2]) 
				characterDirection = 4;
			if (keyHolds[3])
				characterDirection = 0;
			if (keyHolds[0] && keyHolds[3])
				characterDirection = 2;
			if (keyHolds[0] && keyHolds[1])
				characterDirection = 3;
			if (keyHolds[2] && keyHolds[3])
				characterDirection = 5;
			if (keyHolds[2] && keyHolds[1])
				characterDirection = 6;
		
		character = Textures.vlad[0][characterDirection][characterStep];
		}
	}
	
	private void updateCharacterAttack() {
		if (attackTimer++ < 12) 
			character = Textures.vlad[1][characterDirection][attackTimer-1];
		else 
			attacking = false;
		
	}
	//Checks if the shift variable is within acceptable limits,
	//resets them if they aren't
	private void checkShift() {
		if (shiftX > boundsX-width)
			shiftX = boundsX-width;
		if (shiftX < 0)
			shiftX = 0;
		if (shiftY > boundsY-height)
			shiftY = boundsY-height;
		if (shiftY < 0)
			shiftY = 0;
		if(posX < 0)
			posX = 0;
		if (posY < 0)
			posY = 0;
		
		/*
		if (shiftX != 0)	
			posX = width/2;		
		if (shiftY != 0)
			posY = height/2;
			*/	
	}
	
	/*
	public void refreshMapObjects(ArrayList<MapElements> N) {
		onScreenObjs = N;
		changed();
	}
	
	public void refreshMobs(ArrayList<Mobs> M) {
		mobs = M;
	}
	*/
	
	///////////Get methods//////////////
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean getChange() {
		return change;
	}
	
	public int[] getBoundsXY() {
		int[] tmp = new int[2];
		tmp[0] = boundsX;
		tmp[1] = boundsY;
		return tmp;
	}
	
	
	public int[] getPosition() {
		int[] tmp = new int[2];
		tmp[0] = shiftX + posX;
		tmp[1] = shiftY + posY;
		return tmp;
 	}
	
	public int[] getReleventData() {
		int[] tmp = new int[3];
		tmp[0] = shiftX + posX;
		tmp[1] = shiftY + posY;
		tmp[2] = characterDirection;
		return tmp;
	}
	////////////////////////////////////
	
	/////Controls the map shifting//////
	
	private int pixChange = 3;
	
	public void incShiftX() {
		cshiftX += pixChange;
	}
	
	public void incShiftY() {
		cshiftY += pixChange;
	}

	public void decShiftX() {
		cshiftX -= pixChange;
	}

	public void decShiftY() {
		cshiftY -= pixChange;
	}
	
	public void incPosX() {
		cposX += pixChange;
	}
	
	public void incPosY() {
		cposY += pixChange;
	}

	public void decPosX() {
		cposX -= pixChange;
	}

	public void decPosY() {
		cposY -= pixChange;
	}

	

	////////////////////////////////////
	

}
