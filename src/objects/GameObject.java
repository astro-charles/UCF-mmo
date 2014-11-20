package objects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class GameObject {
	private int ID;
	private Rectangle bounds;
	private Rectangle hitBox;
	private BufferedImage texture;
	
	public GameObject(BufferedImage I, int x, int y) {
		bounds = new Rectangle(x,y,I.getWidth(),I.getHeight());
		hitBox = bounds;
		texture = I;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle R) {
		bounds = R;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public void setTexture(BufferedImage I) {
		texture = I;
	}
	
	public void setHitbox(int offsetX, int offsetY, int cutX, int cutY) {
		hitBox = new Rectangle(
				bounds.getLocation().x + offsetX,
				bounds.getLocation().y + offsetY,
				(int)bounds.getWidth() - offsetX - cutX,
				(int)bounds.getHeight() - offsetY - cutY
				);
	}
	public Rectangle getHitBox() {
		return hitBox;
	}
}
