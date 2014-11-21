package server;

import java.net.*;
import java.io.*;
import java.util.*;

import objects.MobPacket;

public class Server extends Thread{
    private ServerSocket server;
	private Socket socket;

	private ArrayList<ClientCommunicator> clients = new ArrayList<ClientCommunicator>();
    private ArrayList<IOpair> clientPair = new ArrayList<IOpair>();
    
	public Server(){
		System.out.println("Makeing server");
		
		try{
			server = new ServerSocket(1010);
		}
		catch(IOException e){
                    System.out.println("Problem starting server");
        }
	}
        
    public static void main(String[] args){
        Server serv = new Server();
        serv.run();
    }
        
        @Override
	public void run(){
		while(true){
			
			/*
			// Remove all disconnected clients
			for(int i = 0;i < clients.size();i++){
				// Check connection, remove on dead
				if(!clients.get(i).isConnected()){
					System.out.println(clients.get(i));
					clients.remove(i);
				}
			}
			*/
		
			checkUnpaired();
			sendMobs();
			
			try{
				socket = server.accept();
			}
			catch(Exception e){
                            System.out.println("Problem making client socket");
            }
			
			if (socket != null) {
				System.out.println("Creating Client Comm");
				clients.add(new ClientCommunicator(socket));
			}
		}
	} 
        
    private void sendMobs() {
    	
    	for (IOpair to : clientPair) {
    		
    		if (to.isFull()) {
    			for (IOpair data : clientPair) {
    				if (to != data) {
    					ClientCommunicator tmp = to.getOutput();
    					tmp.sendMob(data.getMob());
    				}
    			}
    		}
    	}
    	
    }
        
    private void checkUnpaired() {
        	for (ClientCommunicator CC : clients) {
				if (CC.initialized) {
					MobPacket tmp = CC.getMob();
					boolean found = false;
					
					for (IOpair P : clientPair) {
						if (tmp.toString() == P.toString()) {
							if (CC.getComType() == 1)  {
								if (P.getInput() == null) {
									P.setInput(CC);
									clients.remove(CC);
									found = true;
									break;
								}
							}
							else if (CC.getComType() == 0) {
								if (P.getOutput() == null) {
									P.setOutput(CC);
									clients.remove(CC);									
									found = true;
									break;
								}
							}
							System.out.println("Someones logged in with the same credentials...");
						}
						
					}
					
					if (!found) {
						IOpair pair = new IOpair();
						
						if (CC.getComType() == 1)  {
							pair.setInput(CC);
							pair.setMobPacket(CC.getMob());
						}
						else if (CC.getComType() == 0) {
							pair.setOutput(CC);
							pair.setMobPacket(CC.getMob());
						}
						
						clientPair.add(pair);
						
					}
				}
			}
        }
}

    class IOpair {
        	ClientCommunicator in = null;
        	ClientCommunicator out = null;
        	MobPacket mob = null;
        	
        	public boolean isFull() {
        		if (in != null && out != null)
        			return true;
        		else
        			return false;
        	}
        	
        	public void setMobPacket(MobPacket m) {
        		mob = m;
        	}
        	
        	public void setInput(ClientCommunicator in) {
        		this.in = in;
        	}
        	
        	public void setOutput(ClientCommunicator out) {
        		this.out = out;
        	}
        	
        	public ClientCommunicator getOutput() {
        		return out;
        	}
        	
        	public ClientCommunicator getInput() {
        		return in;
        	}
        	
        	public MobPacket getMob() {
        		return mob;
        	}
        	public String toString() {
        		return mob.toString();
        	}
        }

