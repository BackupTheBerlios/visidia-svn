package visidia.misc;

import visidia.algo.*;

/**
 * Message contenant un boolean.
 */
public class BooleanMessage extends Message{
    boolean data;
    
    public BooleanMessage(boolean value){
	this.data = value;
    }
    
    public BooleanMessage(boolean data, MessageType type){
	this.data = data;
	setType (type);
    }

   
    public boolean value(){
	return data;
    }
    
    public boolean data(){
	return data;
    }
    
    /**
     * the reurned object is a new Boolean initialized with the value
     * of data
     *
     **/

    public Object getData(){
	return new Boolean(data);
    }
    
    public Object clone(){
	return new BooleanMessage(data, getType());
    }
    public String toString(){
	return (new Boolean(data)).toString();
    }
    
}
    
