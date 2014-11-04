package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Textures {
	public static BufferedImage[] grass;
	public static BufferedImage[] trees;
	public static BufferedImage[][] Map;
	
	public Textures() throws IOException {
		loadGrass();
		loadTrees();
		generateMap();
	}
	
	//TO-DO: make a better map!
	private void generateMap() {
		Map = new BufferedImage[11][];
		for (int i=0; i<Map.length; i++) {
			Map[i] = new BufferedImage[11];
			for (int j=0; j<Map[i].length; j++) {
				Map[i][j] = grass[3];
			}
		}
		int center = 5;
		for (int i=0; i<3; i++) {
			Map[center-1+i][center-1] = grass[0+i];
			Map[center-1+i][center] = grass[4+i];
			Map[center-1+i][center+1] = grass[9+i];
		}
	
		
		imageViewer(Map);
	}
	
	//TO-DO: Finish loading in all of the trees
	private void loadTrees() throws IOException {
		BufferedImage img = ImageIO.read(new File("images/map/TREE.GIF"));
		
		trees = new BufferedImage[4];
		
		trees[0] = img.getSubimage(15*8, 20*8-1, 10*8, 15*8);
		imageViewer(trees[0]);
		
		imageViewer("images/map/TREE.GIF");
	}

 	private void loadGrass() throws IOException{
		
		//imageViewer("images/map/GRS2ROC.bmp");
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
				g.drawImage(img, 0, 0, Color.BLUE, null);
				
				/*
				for (int i=0; i<img.getWidth()/scale; i++) {
					g.drawLine(i*scale,0, i*scale, img.getHeight());
					
				}
				
				for (int i=0; i<img.getHeight()/scale; i++) {
					g.drawLine( 0, i*scale, img.getWidth(), i*scale);
				}
				*/
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
	}
