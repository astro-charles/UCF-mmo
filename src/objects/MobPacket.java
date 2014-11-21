/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Elucid
 */
public class MobPacket {
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
