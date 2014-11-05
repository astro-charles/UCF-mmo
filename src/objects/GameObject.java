package objects;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class GameObject {
	private int ID;
	private float[] location;
	private Dimension size;
	private BufferedImage texture;
	
	
	public void setDimension(Dimension D) {
		size = D;
	}
	
	public Dimension getDimension() {
		return size;
	}
	
	public void setLocation(float[] L) {
		location = L;
	}
	
	public float[] getLocation() {
		return location;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public void setTexture(BufferedImage I) {
		texture = I;
	}
}
