package server;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientCommunicator{
	private Socket socket;
	private boolean connected;
	private ClientCommunicator.Handler handler;
	private ObjectOutputStream mail;
	
	private class Handler extends Thread{
		private ObjectInputStream in;
		Handler(){
			try{
				in = new ObjectInputStream(socket.getInputStream());
			}
			catch(Exception e){System.out.println("Problem making ObjectStream");};
		}
		
		public void run(){
			
			int count = 0;
			while (true) {
				
	
				if (socket.isClosed() || !socket.isConnected()) {
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
					//if(in.available() != 0){
						String stuff = in.readObject().toString();
						System.out.println(stuff);
					//}
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
		
		try {
			mail = new ObjectOutputStream(socket.getOutputStream());
			mail.writeObject("You are Connected");
			mail.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		handler = new Handler();
		handler.start();
		
		try {
			socket.setKeepAlive(false);;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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