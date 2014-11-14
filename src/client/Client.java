package client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import objects.GameObject;
import util.MathUtil;
import util.Textures;

/*
 * The Client class contains all relevant data for the client and 
 * is in charge of updating the screen, storing game objects, and
 * communicating with the server. It will also handle keyboard 
 * events (use key binding not key listener)
 */

public class Client implements Runnable{
	private ArrayList<GameObject> allMapObjects;
	private static GameGui GUI;

	
	
	@Override
	//Updates client every second
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		Textures.doAll();
		GUI = new GameGui();
		GUI.refreshObjects(MathUtil.genObjects(GUI.getBoundsXY()[0], GUI.getBoundsXY()[1]));
		Thread t = new Thread(GUI);
		t.start();
	}

}
