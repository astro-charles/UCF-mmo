package client;

import java.io.*;
import java.net.*;
public class ServerComm implements Runnable{
	Socket client;
    ObjectInputStream in;
    ObjectOutputStream out;
    
    //ArrayList<Mobs> m = new ArrayList<Mob>();
    ServerComm(){
        
        try{
        	
            client = new Socket("107.161.21.122", 1010);
            //client = new Socket("192.168.1.143", 1010);
            client.setSoTimeout(5000);
            
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            System.out.println("Test");
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
        	
        	
        }
    }
}