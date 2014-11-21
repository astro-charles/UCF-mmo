package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread{
    private ServerSocket server;
	private Socket socket;
	private ArrayList<ClientCommunicator> clients = new ArrayList<ClientCommunicator>();
        

        
	public Server(){
		
		try{
			System.out.println("Creating new Server");
			server = new ServerSocket(1010);
		}
		catch(IOException e){System.out.println(e);}
	}
        
        public static void main(String[] args){
            Server serv = new Server();
            serv.run();
        }
        
        @Override
	public void run(){
        System.out.println("Server starting");
        
        String dots = "";
		while(true){
			
			
			// Remove all disconnected clients
			for(int i = 0;i < clients.size();i++){
				// Check connection, remove on dead
				if(!clients.get(i).isConnected()){
					System.out.println("Disconnecting client");
					System.out.println(clients.get(i));
					clients.remove(i);
				}
			}
			try{
				System.out.print("Testing server connection." + dots + "\r");
				socket = server.accept();
			}
			catch(Exception e){System.out.println(e);}

			if (socket != null) {
				System.out.println("Adding client");
				clients.add(new ClientCommunicator(socket));
			}
			
			dots += ".";
			
			if (dots.length() > 10)
				dots = "";
		}
	}
}
