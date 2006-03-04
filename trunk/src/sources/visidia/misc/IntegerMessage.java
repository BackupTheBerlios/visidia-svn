package visidia.misc;


/**
 * Message contenant un nombre entier.
 */
public class IntegerMessage extends Message{

    /**
     * the message data
     **/
    
    Integer data;
    

    
    public IntegerMessage(Integer data){
	this.data = new Integer(data.intValue());
    }

    public IntegerMessage(int data){
	this.data = new Integer(data);
    }

    public IntegerMessage(Integer data, MessageType type){
	this.data = new Integer(data.intValue());
	setType (type);
    }

    public IntegerMessage(int data, MessageType type){
	this.data = new Integer(data);
	setType(type);
    }


    /**
     * return an int representation of data
     */
    public int value(){
	return data.intValue();
    }

    /**
     * returns an Integer representation of data using : new Integer(data)
     */
    public Integer data(){
	return new Integer(data.intValue());
    }


    /**
     * returns an Object representation (using the Integer clone
     * method) of data. This method is the same as the data()
     * method. Nevertheless, the receive method of class algorithm
     * return a Message object and not an IntegerMessage Object. Thus,
     * to use the data() method, the user must use a cast. The
     * getData() method enables to do the cast when dealing with the
     * message data. This can be convenient in many cases.<br>
     *
     * The returned value is a new Integer initialized with data
     * value.
     **/
    public Integer getData(){
	return new Integer(data.intValue());
    }
    
    public Object clone(){
	return new IntegerMessage(data, getType());
    }

    public String toString(){
	return data.toString();
    }
}
    
