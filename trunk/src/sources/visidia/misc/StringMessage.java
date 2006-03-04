package visidia.misc;


/**
 * Message contenant une chaîne de caractères.
 */
public class StringMessage extends Message{

    /**
     * the message data
     */
    String data;
    
    public StringMessage(String data){
	this.data = new String(data);
    }

    public StringMessage(String data, MessageType type){
	setType (type);
	this.data = new String(data);
    }

    public String data() {
	return new String(data);
    }

    /**
     * the returned message is a new String initialized with data value
     *
     **/
    public String getData() {
	return new String(data);
    }
    
    public Object clone(){
	return new StringMessage(data, getType());
    }

    public String toString(){
	return data;
    }
}
    
