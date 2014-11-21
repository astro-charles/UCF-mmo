/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;
import java.io.*;
/**
 *
 * @author Elucid
 */
public class MobPacket implements Serializable{
    public String name;
    public int posx;
    public int posy;
    public boolean kill;
    
    public MobPacket(String name, int x, int y, boolean kill){
        this.name = name;
        this.posx = x;
        this.posy = y;
        this.kill = kill;
    }
}
