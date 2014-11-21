package server;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientCommunicator{
	private Socket socket;
	private boolean connected;
	private ClientCommunicator.Handler handler;
	//
	private class Handler extends Thread{
		private ObjectInputStream in;
		Handler(){
			try{
				in = new ObjectInputStream(socket.getInputStream());
			}
			catch(Exception e){System.out.println("Problem making ObjectStream");};
		}
		
		public void run(){
			
			while (true) {
				
				if (socket.isClosed() == true) {
					System.out.println("Socket closed");
					break;
				}
					
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try{
					if(in.available() != 0){
						String stuff = in.readObject().toString();
						System.out.println(stuff);
					}
				}
				catch(Exception e){
					System.out.println("Problem reading objectstream");
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
		connected = socket.isConnected();
		
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