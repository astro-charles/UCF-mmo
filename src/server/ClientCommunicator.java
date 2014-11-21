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
	ArrayList<MobPacket> mobs;
	
	
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
				System.out.println((int) message);
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
			
			
			
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				output.writeObject("Booya");
				output.flush();
			} catch (IOException e1) {
				purge();
				e1.printStackTrace();
				return;
			}
			
			if ((String) message == "INPUT")
				comType = 0;
			else if((String) message == "OUTPUT") {
				comType = 1;
			}
			
			
			System.out.println(comType);
			initialized = true;
			while (true) {
				
					
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					break;
				}
				
				
				try {
					if (comType == 0)
						runInput();
					else if (comType == 1)
						runOutput();
					
				
				} catch (ClassNotFoundException | IOException e) {
						purge();
						System.out.println("Client Disconected...");
						break;
						//e.printStackTrace();
				}
				
			}
		}
	}
	
	private void runOutput() throws IOException {
		if (mobs != null) {
			for (MobPacket m : mobs) {
					output.writeObject(m);
			
			}
		}
	}
	
	
	private void runInput() throws ClassNotFoundException, IOException {
		
		Object stuff = input.readObject();
			
			
		if (stuff instanceof int[]) {
			int[] tmp = (int[]) stuff;
			System.out.println("Recieved " + mobP.toString() + " Position: (" + tmp[0] + "," + tmp[1] + ")");		
		}
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