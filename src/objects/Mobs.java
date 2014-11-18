package objects;

import java.awt.image.BufferedImage;
import java.util.Random;


public class Mobs extends GameObject{

	public final int hitCons = 5;
	private int maxHealth;
	private int health;
	private int coins;
	private int lvl;
	
	private int direction;
	private int distance;
	private boolean moving;
	
	public Mobs(BufferedImage I, int x, int y) {
		super(I, x, y);
		
		Random rand = new Random();
		coins = rand.nextInt(10) + 1;
		
		lvl = rand.nextInt(10);
		
		direction = 0;
		moving = false;
	}
	

	public void decDistance() {
		distance -= 1;
	}
	
	public void setDistance(int d) {
		distance = d;
	}
	
	public void move() {
		moving = true;
	}
	
	public void stop() {
		moving = false;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setDirection(int d) {
		if (d > 7 || d < 0)
			return;
		
		direction = d;
	}

}
