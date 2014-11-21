package client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Timer;

import objects.GameObject;
import objects.MapElements;
import objects.Mobs;
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
	private GameGui GUI;
	private ServerComm serv;
	
	public Client() {
		serv = new ServerComm();
		
		if (!serv.connected) {
			System.out.println("Failed to connect.");
			return;
		}
			
		
		Thread s = new Thread(serv);
		
		int mapX = 100*40;
		int mapY = mapX;
		
		Textures.doAll();
		
		ArrayList<MapElements> E = MathUtil.genMap(mapX, mapY);
		ArrayList<Mobs> M = MathUtil.genMobs(E, mapX, mapY);
		
		DrawMoving mov = new DrawMoving(M,E);
		
		GUI = new GameGui(mov, mapX, mapY);
		
		Timer t = new Timer(20, GUI);
		t.start();
	
	}
	
	
	@Override
	//Updates client every second
	public void run() {
		ObjectOutputStream out = serv.getOutput();
		ObjectInputStream in = serv.getInput();
		
		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//if (GUI.getChange())
				try {
					out.writeObject(GUI.getPosition());
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public static void main(String[] args) {
		
		
		Thread client = new Thread(new Client());
		client.start();
	}

}
