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
			int X = M.getBounds().x - M.getBounds().width/2;
			int Y = M.getBounds().y - M.getBounds().height/2;
			
			if (X >= shiftX-buff && X <= shiftX+w+buff && Y >= shiftY-buff && Y <= shiftY+h+buff) {
				g.drawImage(M.getTexture(), X-shiftX, Y-shiftY, null);
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
