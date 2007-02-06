package visidia.misc;


/**
 * Message de synchronisation.
 */
public class SyncMessage extends Message{


    /**
	 * 
	 */
	private static final long serialVersionUID = -4018343518234819536L;

	Integer data;

    public static final MessageType synchronizationType = new MessageType ("synchronization", false);

    public SyncMessage(Integer data){
	this.data = new Integer(data.intValue());
	setType (synchronizationType);
    }

    public SyncMessage(int value){
	this.data = new Integer(value);
	setType (synchronizationType);
    }

    public int value(){
	return data.intValue();
    }

    public Integer data(){
	return new Integer(data.intValue());
    }
    

    /**
     * the returned message is a new Integer initialized with data value
     *
     **/
    public Object getData(){
	return new Integer(data.intValue());
    }

    public Object clone(){
	return new SyncMessage(data);
    }

    public String toString(){
	return "sync "+ data.toString();
    }
}
    
