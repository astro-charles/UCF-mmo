package client;

import java.io.*;
import java.net.*;
public class ServerComm implements Runnable{
    Socket client = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    ServerComm(){
        
        try{
            client = new Socket("107.161.21.122", 1010);
            //in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        }catch(Exception e){System.out.println(e);}
    }
    
    public void run(){
        String test = new String("test");
        try{
        out.writeObject(test);
        }
        catch(Exception e){System.out.println(e);}
    }
}

