package client;

import java.io.*;
import java.net.*;
public class ServerComm implements Runnable{
	Socket clientIN;
	Socket clientOUT;
	
    ObjectInputStream in;
    ObjectOutputStream out;
    
    //ArrayList<Mobs> m = new ArrayList<Mob>();
    ServerComm(){
        
        try{
        	
            clientIN = new Socket("107.161.21.122", 1010);
            clientOUT = new Socket("107.161.21.122", 1010);
            //client = new Socket("192.168.1.143", 1010);
            //client.setSoTimeout(5000);
            
            out = new ObjectOutputStream(clientIN.getOutputStream());
            out.writeObject("Input");
            
            out = new ObjectOutputStream(clientOUT.getOutputStream());
            out.writeObject("Output");
            
            in = new ObjectInputStream(clientIN.getInputStream());
            
            
        }catch(Exception e){System.out.println(e);}
    }
    
    public void run(){
        System.out.println("test:");
        while (true) {
        	try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	Object tmp;
        	try {
				if (in.available() != 0) {
					tmp = in.readObject();
					System.out.println(tmp.toString());
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
       
        	
        	try {
				out.writeObject(new String("Sending stuff"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
}