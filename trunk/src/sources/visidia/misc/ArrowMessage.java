package visidia.misc;

//import visidia.algo.*;

/**
 * Message contenant une chaine de caractere.
 */
public class ArrowMessage extends Message{
    Arrow data;
    
    public ArrowMessage(Arrow data){
	this.data = data;
    }

    public ArrowMessage(Arrow data, MessageType type){
	setType (type);
	this.data = data;
    }

    public Arrow data(){
	return data;
    }
    
    /**
     * the returned message is a new Arrow initialized with the data value
     *
     **/    
    public Object getData(){
	return data;
    }

    public Object clone(){
	return new ArrowMessage(data, getType());
    }

    public String toString(){
        if( data.isMarked ){
            return data.left + " X " + data.right;
        }
        else{
            return data.left + " - " + data.right;
        }
    }

}
    
