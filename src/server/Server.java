package server;

import java.net.*;
import java.io.*;
import java.util.*;

import objects.MobPacket;

public class Server extends Thread{
    private ServerSocket server;
	private Socket socket;
	private boolean writing = false;
	
	//private ArrayList<Socket> socket = new ArrayList<Socket>();
	private static List<ClientCommunicator> clients = Collections.synchronizedList(new ArrayList<ClientCommunicator>());
    //private ArrayList<IOpair> clientPair = new ArrayList<IOpair>();
    
	public Server(){

		clients = new ArrayList<ClientCommunicator>();
		ClientCommunicator.mobs = new ArrayList<MobPacket>();
		
		System.out.println("Makeing server");
		
		try{
			server = new ServerSocket(1011);
		}
		catch(IOException e){
			e.printStackTrace();
            System.out.println("Problem starting server");
            return;
        }
	}
        
    public static void main(String[] args){
        Server serv = new Server();
        serv.run();
    }
        
        @Override
	public void run(){
        	
        while (true) {
        	
        		try {
					Thread.sleep(20);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			
        		try{
    				socket = server.accept();
    			}
    			catch(Exception e){
    				e.printStackTrace();
                    System.out.println("Problem making client socket");
                    break;
    			}
    			
    			System.out.println("Client size" + clients.size());
    			
    			if (socket != null)
    				createConn();
    		}
    	}
        
        public static void forwardObject(Object o) {
        	for (ClientCommunicator c :clients) {
        		if (!c.isClosed()) {
        			if (c.getComType() == 1)
        				c.passObject(o);
        		}
        			
        	}
        }
        private synchronized void createConn() {
    			clients.add(new ClientCommunicator(socket));
        }
}
 
    
    
    

    
     