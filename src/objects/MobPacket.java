/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.*;

public class MobPacket implements Serializable{
    public String name;
    public int posx;
    public int posy;
    public boolean kill;
    public int direction;
    
    public MobPacket(String name, int x, int y, boolean kill){
        this.name = name;
        this.posx = x;
        this.posy = y;
        this.kill = kill;
        direction = 0;
    }
    
    public MobPacket clone() {
    	return new MobPacket(name,posx,posy,kill);
    }
    public void setPosition(int x, int y) {
    	posx = x;
    	posy = y;
    }
    
    public String toString(){
        return "Name: " + this.name;
    }
    
    public void setDirection(int d) {
    	direction = d;
    }
}
