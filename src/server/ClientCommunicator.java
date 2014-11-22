package server;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import objects.MobPacket;

public class ClientCommunicator{
	private Socket socket;
	private boolean connected;
	private ClientCommunicator.Handler handler;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int comType;
	public boolean initialized = false;
	protected MobPacket mobP;
	public static ArrayList<MobPacket> mobs;
	
	
public ClientCommunicator(Socket newSocket){
		
		if (newSocket == null)
			System.out.println("Null socket to client");
		
		if (newSocket.isConnected() != true)
			System.out.println("Client is not connected");
		
		
		socket = newSocket;
		
		handler = new Handler();
		handler.start();
		
	}

	private class Handler extends Thread{

		public void run(){
			connected = false;
			
			System.out.println("Establishing connection...");
			
			Object message = null;
			Object mob = null;
			
			try {
				
				input = new ObjectInputStream(socket.getInputStream());
				message = input.readObject();
				mob = input.readObject();
				
			} catch (ClassNotFoundException | IOException e2) {
				purge();
				e2.printStackTrace();
				return;
			}
				
			if (message instanceof String) {
				System.out.println("Recieved message");
				System.out.println((String) message);
			}
			else {
				System.out.println("Not valid data");
				return;
			}
			
			if (mob instanceof MobPacket) {
				mobP = (MobPacket) mob;
				System.out.println("Got data from " + mobP.toString());
			}
			else	
				System.out.println("This is not a mob packet");
			
			
			
			
			
			String mes = (String) message;
			if (mes.equals("INPUT") ){
				comType = 0;
				mobs.add(mobP);
				System.out.println("Seting as input");
				
			}
			else if(mes.equals("OUTPUT")) {
				comType = 1;
				System.out.println("Seting as output");
			
				try {
					output = new ObjectOutputStream(socket.getOutputStream());
					output.writeObject("Booya");
					output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			System.out.println(comType);
			initialized = true;
			while (true) {
				try {
					if (comType == 0)
						runInput();
					
					else if (comType == 1)
						runOutput();
					
				
				} catch (ClassNotFoundException | IOException e) {
						purge();
						removeSelf(mobP);
						System.out.println("Client Disconected...");
						break;
						//e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
					
					purge();
					removeSelf(mobP);
					System.out.println("Client Disconected...");
					break;
				}
				
			}
		}
	}
	
	private synchronized void runOutput() throws IOException, InterruptedException {
		
		Thread.sleep(50);
	
		if (mobs != null) {
			for (MobPacket m : mobs) {
				if (!m.toString().equals(mobP.toString()))
					output.writeObject(m.clone());
					output.flush();
			
			}
		}
	}
	
	
	private void runInput() throws ClassNotFoundException, IOException {
		
		//System.out.println("Runnung input");
		Object stuff = input.readObject();
		//System.out.println("Read?");
			
		if (stuff instanceof int[]) {
			int[] tmp = (int[]) stuff;
			System.out.println("Recieved " + mobP.toString() + " Position: (" + tmp[0] + "," + tmp[1] + ") and direction: " + tmp[2]);	
			mobP.setPosition(tmp[0], tmp[1]);
			mobP.setDirection(tmp[2]);
			
		}
		
		//System.out.println("Stuff?");
	}
	
	public void removeSelf(MobPacket m) {
		MobPacket tmp = null;
		for (MobPacket n : mobs) {
			if (m.toString().equals(n.toString()))
				tmp = n;
		}
		
		if (tmp != null)
			mobs.remove(tmp);
	}
	
	public void setMobs(ArrayList<MobPacket> m) {
		mobs = m;
	}
	
	public void sendMob(MobPacket p) {
		try {
			output.writeObject(p);
			output.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to send ti client");
		}
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	public MobPacket getMob() {
		return mobP;
	}
	public int getComType() {
		return comType;
	}
	
	public void purge(){
	// Close everything
		try{
			connected = false;
			socket.close();
		}
		catch(Exception e){
				System.out.println(e);
		}
	}
	
	public String toString(){
		return new String(socket.toString());
	}
}