package client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/*
 * The Client class contains all relevant data for the client and 
 * is in charge of updating the screen, storing game objects, and
 * communicating with the server.
 */

public class Client implements Runnable, KeyListener{

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		try {
			new Textures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
