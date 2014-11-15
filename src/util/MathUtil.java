package util;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import objects.GameObject;

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
	
	public static ArrayList<GameObject> genObjects(int w, int h) {
		
		w = w/40;
		h = h/40;
		
		if( Textures.trees == null)
			try {
				Textures.loadTrees();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		ArrayList<GameObject> C = new ArrayList<GameObject>();
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				if (i == 0 || j == 0 || i == w-1 || j == h-1) {
					GameObject tmp = new GameObject();
					tmp.setLocation(new float[] {i*40-3*8,j*40-3*8});
					tmp.setDimension(new Dimension(10,20));
					tmp.setTexture(Textures.trees[0]);
					C.add(tmp);
				}
			}
		}
		
		//Attempt at perlin noise
		for (int i=0; i<w; i++){
			for (int j=0; j<h; j++) {
				
				if (MathUtil.perlinPoint(i, j, 100) == 1) {
					GameObject tmp = new GameObject();
					tmp.setLocation(new float[] {i*40,j*40});
					tmp.setDimension(new Dimension(10,20));
					tmp.setTexture(Textures.trees[0]);
					C.add(tmp);
				}
			}
		}
		
		return C;
	}
}
