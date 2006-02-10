package visidia.misc;

import visidia.algo.*;

/**
 * Message contenant un nombre entier.
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
    
    public Object clone(){
	return new BooleanMessage(data, getType());
    }
    public String toString(){
	return (new Boolean(data)).toString();
    }
    
}
    
