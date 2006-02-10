package visidia.simulation;

/**
 * represente une porte d'envoie ou de reception de message.
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
