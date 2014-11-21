package client;
import java.util.*;
import java.io.*;
import java.net.*;
import objects.*;
import util.*;
public class ServerComm implements Runnable{
	
	public boolean connected = true;
	Socket clientIN;
	Socket clientOUT;
	
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private MobPacket character;
    ArrayList<Mobs> mobs = new ArrayList<>();
    ServerComm(ArrayList<Mobs> mobs){
        this.mobs = mobs;
        try{
        	
            clientIN = new Socket("107.161.21.122", 1010);
            clientOUT = new Socket("107.161.21.122", 1010);
            //client = new Socket("192.168.1.143", 1010);
            //client.setSoTimeout(5000);
            character = new MobPacket(Long.toString(System.currentTimeMillis()),
                                      640, 360, false);
            out = new ObjectOutputStream(clientIN.getOutputStream());
            out.writeObject("Input");
            out.writeObject(character);
            out = new ObjectOutputStream(clientOUT.getOutputStream());
            out.writeObject("Output");
            out.writeObject(character);
            
            in = new ObjectInputStream(clientIN.getInputStream());
            
            
        }catch(Exception e){System.out.println(e); connected = false;}
    }
    
    public void run(){
        System.out.println("test:");
        while (true){
        	try{
                    Thread.sleep(20);
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
        	
        	Object temp;
                
                try {
                    
                    temp = in.readObject();
                    
                    System.out.println(temp);
                    if(temp != null && temp instanceof MobPacket){
                        MobPacket tmp = (MobPacket)temp;
                        System.out.println("PACKET Name: " + " " + tmp.name + " posx: " + tmp.posx + " posy:" + tmp.posy);
                        boolean found = false;
                        for(Mobs m : mobs){
                            if(m.getName().compareTo(tmp.name) == 0){
                                found = true;
                                if(tmp.kill == true)
                                    mobs.remove(m);
                                else 
                                    m.setLocation(tmp.posx, tmp.posy);
                            }
                        }
                        if(!found){
                            if(tmp.name.contains("skel"))
                                mobs.add(new Mobs(Textures.skell[2][0][0], tmp.posx, tmp.posy, tmp.name));
                            else
                                mobs.add(new Mobs(Textures.vlad[0][0][0], tmp.posx, tmp.posy, tmp.name));
                        }
                    }
                } 
                catch (ClassNotFoundException | IOException e) {
                    System.out.println(e);
                }

        	try {
                out.writeObject(new String("Sending stuff"));
                } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                }
        	
        }
    }

    public ObjectInputStream getInput() {
    	return in;
    }
    
    public ObjectOutputStream getOutput() {
    	return out;
    }
}