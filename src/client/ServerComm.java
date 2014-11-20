package client;

import java.io.*;
import java.net.*;
public class ServerComm {

    public static void main(){
        Socket client = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try{
            client = new Socket("localhost", 1010);
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        }catch(Exception e){System.out.println(e);}
    }
}

