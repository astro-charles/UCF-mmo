package util;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import objects.GameObject;
import objects.MapElements;

public class MathUtil {
	
	//Generates a sudo-random number
	public static int randomNum(int x, int y) {
		int var1 = 1234500;
		int var2 = 5894838;
		int var3 = 4958674;
		
		return (((var2 * x<<2) + var1 +  (var3 * y>>4))*x / (y+1) * 5)+1;
	}
	
	//Function for random terrain generation, not really important
	public static int perlinPoint(int x, int y, int size) {
		int value = 0, initialSize = size;
	    
	    while(size >= 1)
	    {
	        value += randomNum(x / size, y / size) * size;
	        size /= 2.0;
	    }
	    return (value / initialSize)%2;
	}
	
	public static ArrayList<MapElements> genMap(int w, int h) {
		
		w = w/40;
		h = h/40;
		
		if( Textures.trees == null)
			try {
				Textures.loadTrees();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		ArrayList<MapElements> C = new ArrayList<MapElements>();
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				if (i == 0 || j == 0 || i == w-1 || j == h-1) {
					MapElements tmp = new MapElements(Textures.trees[0],i*40-3*8,j*40-3*8);
					tmp.setHitbox(16, tmp.getBounds().height/2, 16, 8);
					C.add(tmp);
				}
			}
		}
		
		//Perlin noise
		for (int i=0; i<w; i++){
			for (int j=0; j<h; j++) {
				
				if (MathUtil.perlinPoint(i, j, 100) == 1) {
					MapElements tmp = new MapElements(Textures.trees[0], i*40, j*40);
					tmp.setHitbox(16, tmp.getBounds().height/2, 16, 8);
					C.add(tmp);
				}
			}
		}
		
		return C;
	}
}
