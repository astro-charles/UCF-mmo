package client;
import java.util.*;
import java.io.*;
import java.net.*;

import objects.*;
import util.*;
public class ServerComm implements Runnable{
	
	public boolean connected = true;
	private Socket clientIN;
	private Socket clientOUT;
	
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private MobPacket character;
    ArrayList<Mobs> mobs = new ArrayList<>();
    public static boolean mobChange = false;
    
    ServerComm(ArrayList<Mobs> mobs, int x, int y, String name){
        this.mobs = mobs;
        try{
        	
            clientIN = new Socket("107.161.21.122", 1011);
            clientOUT = new Socket("107.161.21.122", 1011 );
            //clientIN = new Socket("localhost", 1011);
            //clientOUT = new Socket("localhost", 1011);
            
            character = new MobPacket("Bob" +Long.toString(System.currentTimeMillis()/10000),
                                      x, y, false, 0);
            
            out = new ObjectOutputStream(clientIN.getOutputStream());
            out.writeObject("OUTPUT");
            out.writeObject(character);
            out.flush();
            //clientIN.shutdownOutput();
        
            out = new ObjectOutputStream(clientOUT.getOutputStream());
            out.writeObject("INPUT");
            out.writeObject(character);
            out.flush();
           // clientOUT.shutdownInput();
            
            System.out.println("Making input stream...");
            in = new ObjectInputStream(clientIN.getInputStream());
            
            
        }catch(Exception e){System.out.println(e); connected = false;}
    }
    
    public void run(){
        System.out.println("test:");
        
        int lastx = 0;
        int lasty = 0;
        
        while (true){
        	try{
                    Thread.sleep(20);
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
        	
        	Object temp;
                
                try {
                	//System.out.println("Attempting to read...");
                    temp = in.readObject();
                    //System.out.println("Object read");
                    
                    //System.out.println(temp);
                    if(temp instanceof MobPacket){
                        MobPacket tmp = (MobPacket)temp;
                        System.out.println("PACKET Name: " + " " + tmp.name + " posx: " + tmp.posx + " posy:" + tmp.posy + " direction: " + tmp.direction);
                        boolean found = false;
                        for(Mobs m : mobs){
                            if(m.getName().compareTo(tmp.name) == 0){
                            	
                            	if (m.getBounds().x == tmp.posx && m.getBounds().y == tmp.posy)
                            		mobChange = false;
                            	else
                            		mobChange = true;
                            	
                            	System.out.println("direction = " + tmp.direction);
                            	if (m.getDirection() != tmp.direction) {
                            		System.out.println("Changeing direction");
                            		m.setTexture(Textures.vlad[0][tmp.direction][3]);
                            	}
                            	
                            	System.out.println("Found = true");
                                found = true;
                                if(tmp.kill == true)
                                    mobs.remove(m);
                                else  {
                                    m.setLocation(tmp.posx, tmp.posy);
                                    m.setDirection(tmp.direction);
                                }
                            }
                        }
                        if(!found){
                            if(tmp.name.contains("skel"))
                                mobs.add(new Mobs(Textures.skell[2][0][0], tmp.posx, tmp.posy, tmp.name));
                            else
                                mobs.add(new Mobs(Textures.vlad[0][tmp.direction][3], tmp.posx, tmp.posy, tmp.name));
                        }
                    }
                    
                    else if (temp instanceof String) {
                    	System.out.println("Client recieved string");
                    	ChatPannel.addString((String) temp);
                    }
                } 
                catch (ClassNotFoundException | IOException e) {
                	System.out.println("Failed to read...");
                    System.out.println(e);
                    break;
                }

        }
    }
    
    public static void writeMessage(String s) {
    	try {
			out.writeObject(s);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    	

    public ObjectInputStream getInput() {
    	return in;
    }
    
    public ObjectOutputStream getOutput() {
    	return out;
    }
}