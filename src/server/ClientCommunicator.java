package server;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientCommunicator{
	private Socket socket;
	private boolean connected;
	private ClientCommunicator.Handler handler;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int comType;
	
	private class Handler extends Thread{

		public void run(){
			connected = false;
			
			System.out.println("Establishing connection...");
			
			Object message = null;
			
			try {
				
				input = new ObjectInputStream(socket.getInputStream());
				message = input.readObject();
				
			} catch (ClassNotFoundException | IOException e2) {
			
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
			
			
			
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				output.writeObject("You are Connected");
				output.flush();
			} catch (IOException e1) {
				
				e1.printStackTrace();
				return;
			}
			
			if ((String) message == "Input")
				comType = 0;
			else if((String) message == "Output")
				comType = 1;
			
			while (true) {
				
	
				if (socket.isClosed() || !socket.isConnected()) {
					System.out.println("Socket closed");
					break;
				}
					
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					break;
				}
				
				try{
					Object stuff = input.readObject();
					
					
					if (stuff instanceof int[]) {
						System.out.println("Got position");
						int[] tmp = (int[]) stuff;
						System.out.println("Recieved Position: (" + tmp[0] + "," + tmp[1] + ")");
						
							
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("Problem reading objectstream.\n Probably disconnected...");
					break;	
				}
			}
		}
	}
	
	public ClientCommunicator(Socket newSocket){
		
		if (newSocket == null)
			System.out.println("Null socket to client");
		
		if (newSocket.isConnected() != true)
			System.out.println("Client is not connected");
		
		
		socket = newSocket;
		
		handler = new Handler();
		handler.start();
		
	}
	
	public boolean isConnected(){
		return connected;
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