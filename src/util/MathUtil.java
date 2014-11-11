package util;

public class MathUtil {
	
	//Generates a sudo-random number
	public static int randomNum(int x, int y) {
		int var1 = 1234500;
		int var2 = 5894838;
		int var3 = 4958674;
		
		return (((var2 * x<<2) + (var3 * y>>4))*x / (y+1) * 5)+1;
	}
	
	//Function for random terrain generation, not really important
	public static int perlinPoint(int x, int y, int size) {
		System.out.println("Perlin Point!");
		int value = 0, initialSize = size;
	    
	    while(size >= 1)
	    {
	        value += randomNum(x / size, y / size) * size;
	        size /= 2.0;
	    }
	    System.out.println(128 * value / initialSize);
	    return (value / initialSize)%2;
	}
}
