package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import objects.MapElements;
import objects.Mobs;

public class DrawMoving implements Runnable{

	public boolean changeHere = false;
	private ArrayList<Mobs> mobs;
	private ArrayList<MapElements> map;
	private int buff = 100;
	
	public DrawMoving(ArrayList<Mobs> m, ArrayList<MapElements> e) {
		mobs = m;
		map = e;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void drawMobs(Graphics2D g, int shiftX, int shiftY, int w, int h) {
		for (Mobs M : mobs) {
			int X = M.getBounds().x;
			int Y = M.getBounds().y;
			
			if (X >= shiftX-buff && X <= shiftX+w+buff && Y >= shiftY-buff && Y <= shiftY+h+buff) {
				g.drawImage(M.getTexture(), X-shiftX, Y-shiftY, null);
				//g2.setColor(Color.YELLOW);
				//g2.drawRect(G.getBounds().x - shiftX, G.getBounds().y - shiftY, G.getBounds().width, G.getBounds().height);
				//g.setColor(Color.CYAN);
				//g.drawRect(M.getHitBox().x - shiftX, M.getHitBox().y - shiftY, M.getHitBox().width, M.getHitBox().height);
			}
		}
	}
	
	public void drawObjects(Graphics2D g2, int shiftX, int shiftY, int w, int h) {
		if (map != null) {
			for (MapElements G : map) {
				int X = G.getBounds().x;
				int Y = G.getBounds().y;
				
				if (X >= shiftX-buff && X <= shiftX+w+buff && Y >= shiftY-buff && Y <= shiftY+h+buff) {
					g2.drawImage(G.getTexture(), X-shiftX, Y-shiftY, null);
					//g2.setColor(Color.YELLOW);
					//g2.drawRect(G.getBounds().x - shiftX, G.getBounds().y - shiftY, G.getBounds().width, G.getBounds().height);
					g2.setColor(Color.CYAN);
					g2.drawRect(G.getHitBox().x - shiftX, G.getHitBox().y - shiftY, G.getHitBox().width, G.getHitBox().height);
				}
			}
		}
	}
	
	public boolean detectCollision(Rectangle R) {
		
		for (MapElements m : map) {
			if (m.getHitBox().intersects(R)) {
				//System.out.println("Collision");
				return true;
			}
		
		}
		
		return false;
	}
	

}
