package fr.enserb.das.misc;


/**
 * Message contenant une chaine de caractere.
 */
public class StringMessage extends Message{
    String data;
    
    public StringMessage(String data){
	this.data = new String(data);
    }

    public StringMessage(String data, MessageType type){
	setType (type);
	this.data = new String(data);
    }

    public String data(){
	return new String(data);
    }

    public Object clone(){
	return new StringMessage(data, getType());
    }

    public String toString(){
	return data;
    }
}
    
