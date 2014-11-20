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
			server = new ServerSocket(1010);
		}
		catch(IOException e){System.out.println(e);}
	}
        
        public static void main(){
            Server serv = new Server();
            serv.run();
        }
        
        @Override
	public void run(){
		while(true){
			// Remove all disconnected clients
			for(int i = 0;i < clients.size();i++){
				// Check connection, remove on dead
				if(!clients.get(i).isConnected()){
					System.out.println(clients.get(i));
					clients.remove(i);
				}
			}
			try{
				socket = server.accept();
			}
			catch(Exception e){System.out.println(e);}

			clients.add(new ClientCommunicator(socket));
		}
	}
}


