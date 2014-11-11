package client;
/*
 * Helper class to load in images for the game, view those images, generate a map for the game,
 * and test out how to display the graphics
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import objects.GameObject;
import util.MathUtil;

public class Textures {
	public static BufferedImage[] grass;	//Grass images
	public static BufferedImage[] trees;	//Tree images
	public static BufferedImage Map;	//Array of images making up the map
	private int RGBfilter = -11836545;		//Integer value of color to filter
	private int mapSize = 100;
	private static int startX = 0;			//Temporary variables for the map drawer to use
	private static int startY = 0;			//
	private static int shiftX = 0;
	private static int shiftY = 0;
	
	public Textures() throws IOException {
		loadGrass();
		loadTrees();
		generateMap();
	}
	
	/*
	 *Generates the map for the game. 
	 * Will only be used once when finished and
	 * it will be outputed to a file
	*/
	private void generateMap() {
		
		Map = new BufferedImage(40*mapSize, 40*mapSize, BufferedImage.TYPE_4BYTE_ABGR);
		
		BufferedImage[][] tiles = new BufferedImage[mapSize][];
		for (int i=0; i<tiles.length; i++) {
			tiles[i] = new BufferedImage[mapSize];
			for (int j=0; j<tiles[i].length; j++) {
				if (i == j)
					tiles[i][j] = grass[5];
				else
					tiles[i][j] = grass[3];
			}
		}
		
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				Map.getGraphics().drawImage(tiles[i][j], i*40, j*40, null);
			}
		}
		
		
		
		ArrayList<GameObject> C = new ArrayList<GameObject>();
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				if (i == 0 || j == 0 || i == tiles.length-1 || j == tiles.length-1) {
					GameObject tmp = new GameObject();
					tmp.setLocation(new float[] {i*40-3*8,j*40-3*8});
					tmp.setDimension(new Dimension(10,20));
					tmp.setTexture(trees[0]);
					C.add(tmp);
				}
			}
		}
		
		//Attempt a perlin noise
		for (int i=0; i<mapSize; i++){
			for (int j=0; j<mapSize; j++) {
				System.out.println(MathUtil.perlinPoint(i, j, 16));
				if (MathUtil.perlinPoint(i, j, 100) == 1) {
					GameObject tmp = new GameObject();
					tmp.setLocation(new float[] {i*40,j*40});
					tmp.setDimension(new Dimension(10,20));
					tmp.setTexture(trees[0]);
					C.add(tmp);
				}
			}
		}
		
	
		GameObject[] T = new GameObject[C.size()];
		mapViewer(C.toArray(T));
	}
	
	private void loadTrees() throws IOException {
		BufferedImage img = ImageIO.read(new File("images/map/TREE.GIF"));
		
		trees = new BufferedImage[1];
		
		trees[0] = img.getSubimage(15*8, 20*8-1, 10*8, 15*8);
		trees[0] = toBufferedImage(makeColorTransparent(trees[0], Color.getColor(null, RGBfilter)));
	}

 	private void loadGrass() throws IOException{
		
		imageViewer("images/map/GRS2ROC.bmp");
		BufferedImage img = ImageIO.read(new File("images/map/GRS2ROC.bmp"));
		
		grass = new BufferedImage[15];
		
		int w = 5*8;
		int h = w;
		
		grass[0] = img.getSubimage(5*8, 10*8+1, w, h);	//Top Left Corner
		grass[1] = img.getSubimage(15*8, 10*8+1, w, h);	//Top border
		grass[2] = img.getSubimage(25*8, 10*8+1, w, h);	//Top Right Corner
		grass[3] = img.getSubimage(35*8, 10*8+1, w, h);	//Grass
		grass[4] = img.getSubimage(5*8, 20*8+1, w, h);	//Left border
		grass[5] = img.getSubimage(15*8, 20*8+1, w, h);	//Stone
		grass[6] = img.getSubimage(25*8, 20*8+1, w, h);	//Right border
		grass[7] = img.getSubimage(35*8, 20*8+1, w, h);	//Top Left big
		grass[8] = img.getSubimage(45*8, 20*8+1, w, h);	//Top Right big
		grass[9] = img.getSubimage(5*8, 30*8+1, w, h);	//bottom left
		grass[10] = img.getSubimage(15*8, 30*8+1, w, h);	//bottom border
		grass[11] = img.getSubimage(25*8, 30*8+1, w, h);	//bottom right
		grass[12] = img.getSubimage(35*8, 30*8+1, w, h);	//bottom left big
		grass[13] = img.getSubimage(45*8, 30*8+1, w, h);	//bottom right big
		
	}
	
	public static void imageViewer(String s) throws IOException {
		BufferedImage img = ImageIO.read(new File(s));
		
		
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = j.getContentPane();
		JPanel p = new JPanel() {
			protected void paintComponent(Graphics g) {
				
				int scale = 8;
				g.drawImage(img, 0, 0, Color.BLUE, null);
				
				for (int i=0; i<img.getWidth()/scale; i++) {
					g.drawLine(i*scale,0, i*scale, img.getHeight());
					
				}
				
				for (int i=0; i<img.getHeight()/scale; i++) {
					g.drawLine( 0, i*scale, img.getWidth(), i*scale);
				}
			}
		};
		
		p.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
		c.add(p);
		j.pack();
		j.setVisible(true);
	}
	
	public static void imageViewer(BufferedImage img){		
		
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = j.getContentPane();
		JPanel p = new JPanel() {
			protected void paintComponent(Graphics g) {
				
				int scale = 8;
				g.drawImage(img, 0, 0, Color.RED, null);
			}
		};
		
		p.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
		c.add(p);
		j.pack();
		j.setVisible(true);
	}
	
	public static void imageViewer(BufferedImage[][] img){		
		
		
		if (img == null) {
			System.out.println("Null image");
			return;
		}
		
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		int width = img[0][0].getWidth()*img.length;
		int height = width;
		
		Container c = j.getContentPane();
		
		JPanel p = new JPanel() {
			protected void paintComponent(Graphics g) {
				
				
				for (int i=0; i<img.length; i++) {
					for (int j=0; j<img[i].length; j++) {
						g.drawImage(img[i][j], i*8*5, j*8*5, Color.red, null);
					}
				}
				
			
			}
		};
		
		p.setPreferredSize(new Dimension(width,height));
		c.add(p);
		j.pack();
		j.setVisible(true);
	}

	public static void mapViewer(GameObject[] obj){		
		
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		int width = 600;
		int height = 600;
		
		Container c = j.getContentPane();
		
		JPanel p = new JPanel() {
			public final int buff = 125;
			//private final BufferedImage buffI = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			
			protected void paintComponent(Graphics g) {
				//Graphics2D bg = (Graphics2D) buffI.getGraphics();
				
				/*
				//Draws the ground tiles
				for (int i=0; i<width/(40); i++) {
					for (int j=0; j<height/(40); j++) {
						if (shiftX < 0 && startX > 0)
							bg.drawImage(img[startX-1 + i][startY+ j], i*40+shiftX, j*40+shiftY, Color.red, null);
						if (shiftY < 0 && startY > 0)
							bg.drawImage(img[startX + i][startY-1 + j], i*40+shiftX, j*40+shiftY, Color.red, null);
						
							bg.drawImage(img[startX + i][startY+ j], i*40+shiftX, j*40+shiftY, Color.red, null);
					}
				}
				*/
				
				g.drawImage(Map.getSubimage(shiftX, shiftY, width, height),0,0,null);
				//Draws game objects, checks if they are in the screen view 
				for (GameObject G : obj) {
					int X = (int)G.getLocation()[0];
					int Y = (int)G.getLocation()[1];
					
					if (X >= shiftX-buff && X <= shiftX+width+buff && Y >= shiftY-buff && Y <= shiftY+height+buff)
						g.drawImage(G.getTexture(), X-shiftX, Y-shiftY, null);
				}
				
			
				
				g.setColor(Color.RED);
				g.drawString("X = " + shiftX + " Y = " +shiftY , 10, 10);
				
			}
		};
		
		p.setPreferredSize(new Dimension(width,height));
		c.add(p);
		
		//Key listener to move around map
		j.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			
				System.out.println(arg0.getKeyChar());
				switch (arg0.getKeyChar()) {
				
				case 'w':
					shiftY -= 10;
					break;
				case 'a':
					shiftX -= 10;
					break;
				case 's':
					shiftY += 10;
					break;
				case 'd':
					shiftX += 10;
					break;
				}
				
				if (shiftX < 0)
					shiftX = 0;
				if (shiftY < 0)
					shiftY = 0;
				
				j.repaint();
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		j.setPreferredSize(new Dimension(width-50,height-50));
		j.pack();
		j.setVisible(true);
	}

	public static Image makeColorTransparent (Image im, final Color color) {
 
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;
   
			public final int filterRGB(int x, int y, int rgb) {
		                     
				if ( ( rgb | 0xFF000000 ) == markerRGB ) {
		                                        
					return 0x00FFFFFF & rgb;
		                                               
				}
		                        
				else {
		                        
					// nothing to do
		                       
					return rgb;
		                     
				}
		                     
			}
		                   
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	
	    
	}
	
	public static BufferedImage toBufferedImage(Image img)	{
	    
    	if (img instanceof BufferedImage) {
	        return (BufferedImage) img;
    	}
		     
    	BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    	Graphics2D bGr = bimage.createGraphics();
			             
    	bGr.drawImage(img, 0, 0, null);
    	bGr.dispose();
    	
    	return bimage;
			                             
    }
	    
	
}
	
