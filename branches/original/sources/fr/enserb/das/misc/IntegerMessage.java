package fr.enserb.das.misc;


/**
 * Message contenant un nombre entier.
 */
public class IntegerMessage extends Message{
    Integer data;
    
    public IntegerMessage(Integer data){
	this.data = new Integer(data.intValue());
    }

    public IntegerMessage(int value){
	this.data = new Integer(value);
    }

    public IntegerMessage(Integer data, MessageType type){
	this.data = new Integer(data.intValue());
	setType (type);
    }

    public IntegerMessage(int value, MessageType type){
	this.data = new Integer(value);
	setType (type);
    }


    public int value(){
	return data.intValue();
    }

    public Integer data(){
	return new Integer(data.intValue());
    }

    public Object clone(){
	return new IntegerMessage(data, getType());
    }

    public String toString(){
	return data.toString();
    }
}
    
