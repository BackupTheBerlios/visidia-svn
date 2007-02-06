package visidia.misc;


/**
 * Message contenant une chaîne de caractères.
 */
public class StringMessage extends Message{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6308012798229593782L;
	/**
     * the message data
     */
    String data;
    
    public StringMessage(String data){
	this.data = new String(data);
    }

    public StringMessage(String data, MessageType type){
	this.setType (type);
	this.data = new String(data);
    }

    public String data() {
	return new String(this.data);
    }

    /**
     * the returned message is a new String initialized with data value
     *
     **/
    public String getData() {
	return new String(this.data);
    }
    
    public Object clone(){
	return new StringMessage(this.data, this.getType());
    }

    public String toString(){
	return this.data;
    }
}
    
