package visidia.misc;

import visidia.rule.*;
import java.io.Serializable;


/**
 * Message contenant un Label et etiquete Mark.
 */

public class NeighbourMessage extends Message implements Serializable{
    
    private boolean mark=false;
    private String  label="";
    
        
    public NeighbourMessage( String l, boolean b){
	
	this.mark=b;
	this.label=l;
    }
    
    public NeighbourMessage(Neighbour n){
	
	this.mark=n.mark();
	this.label=n.state();
    
    }
    public NeighbourMessage(Neighbour n, MessageType t){
	
	this(n);
	setType(t);
    
    }  
    
    public boolean mark(){
	return mark;
    }
    
    public String label(){
	return label;
    }
    
    public Neighbour getNeighbour(){
	
	return	new Neighbour(label, mark);
    }

    /**
     * the returned object is a clone of this message.
     *
     **/

    
    public Object getData() {
	return new Neighbour(label, mark);
    }
    
    public Object clone(){
	NeighbourMessage n= new NeighbourMessage(label(), mark());
	n.setType(this.getType());
	return n;
    }
   
    public String toString(){
        if(mark() )
            return "-X-"+label();
	return "---"+label();
    }
}

    
