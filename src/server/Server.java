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
	private List<ClientCommunicator> clients = Collections.synchronizedList(new ArrayList<ClientCommunicator>());
    //private ArrayList<IOpair> clientPair = new ArrayList<IOpair>();
    
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
        	
        Thread connector = new Thread() {
        	
        	public void run() {
        		
        		while (true) {
        			try{
        				socket = server.accept();
        			}
        			catch(Exception e){
                         System.out.println("Problem making client socket");
        			}
        			
        			System.out.println("Client size" + clients.size());
        			
        			if (socket != null)
        				createConn();
        		}
        	}
        };
        
        connector.start();
        
        MobPacket no = new MobPacket("TESTING", 0, 0, false);
        
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
		
			//checkUnpaired();
			createConn();
			sendMobs();
			
	
		}
	} 
        
    private synchronized void createConn() {
    			clients.add(new ClientCommunicator(socket));
    }
 
    
    
    
    
    /*
    private synchronized void sendMobs() {
    	ArrayList<MobPacket> tmp = new ArrayList<MobPacket>();
    
    	//while (writing){};
    	
    	
    	for (ClientCommunicator in : clients) {
    		if (in.initialized && in.getComType() == 1) {
    			System.out.println("Adding to array");
    			tmp.add(in.getMob());
    		}
    			
    	}
    	
    	for (ClientCommunicator out : clients) {
    		if (out.initialized && out.getComType() == 0) {
    			System.out.println("Sending out Mobs");
    			for (MobPacket mob : tmp)
    				out.sendMob(mob);
    		}
    			
    	}
    	
    }
}
*/
        
    private synchronized void sendMobs() {
    	if (clients.size() == 0 || clients.size() == 1)
    		return;
    	
    	for (ClientCommunicator to : clients) {
    		if (to == null)
    			continue;
    		if (to.getComType() == 0) {
    			for (ClientCommunicator data : clients) {
    				if (to != data) {
    					if (data.getComType() == 1) {
    						to.sendMob(data.getMob());
    					}
    				}
    			}
    		}
    	}
    }
}
    
     /*
    private void sendMobs() {
    	//System.out.println("Sending Mobs to Everyone");
    	
    	for (IOpair to : clientPair) {
    		
    		if (to.isFull()) {
    			System.out.println("Sending to " + to.toString());
    			for (IOpair data : clientPair) {
    				if (to != data) {
    					ClientCommunicator tmp = to.getOutput();
    					tmp.sendMob(data.getMob());
    				}
    			}
    		}
    	}
    	
    }
        /*
    private void checkUnpaired() {
        	for (ClientCommunicator CC : clients) {
				if (CC.initialized) {
					
					MobPacket tmp = CC.getMob();
					boolean found = false;
					
					for (IOpair P : clientPair) {
						if (tmp.toString() == P.toString()) {
							System.out.println("Found matching pair " + P.toString());
							
							if (CC.getComType() == 1)  {
								System.out.println("Input type");
								if (P.getInput() == null) {
									P.setInput(CC);
									clients.remove(CC);
									found = true;
									break;
								}
							}
							else if (CC.getComType() == 0) {
								System.out.println("Output type");
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
						
						System.out.println("Creating new pair: " + pair.toString());
						clientPair.add(pair);
						
					}
				}
			}
        }
}

/*
    class IOpair {
        	ClientCommunicator in = null;
        	ClientCommunicator out = null;
        	MobPacket mob = null;
        	
        	public boolean isFull() {
        		if (in != null && out != null)
        			return true;
        		else {
        			return false;
        		}
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
        */

