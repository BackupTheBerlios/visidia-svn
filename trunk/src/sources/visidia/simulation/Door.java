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
	doorNum = num;
    }


    public int getNum(){
	return doorNum;
    }

    public void setNum(int num){
	doorNum = num;
    }
}	
