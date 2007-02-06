package visidia.simulation;

/**
 * represents a door of some node. a door can be a reception door or a
 * sending door. This is used in the MessagePacket class.
 */
public class Door {
    private int doorNum;
    
    public Door(){
	this(0);
    }


    public Door(int num){
	this.doorNum = num;
    }


    public int getNum(){
	return this.doorNum;
    }

    public void setNum(int num){
	this.doorNum = num;
    }
}	
