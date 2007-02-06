package visidia.misc;

import visidia.rule.*;
import java.io.Serializable;


/**
 * Message contenant un Label et etiquete Mark.
 */

public class NeighbourMessage extends Message implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3518808742691710901L;
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
	this.setType(t);
    
    }  
    
    public boolean mark(){
	return this.mark;
    }
    
    public String label(){
	return this.label;
    }
    
    public Neighbour getNeighbour(){
	
	return	new Neighbour(this.label, this.mark);
    }

    /**
     * the returned object is a clone of this message.
     *
     **/

    
    public Object getData() {
	return new Neighbour(this.label, this.mark);
    }
    
    public Object clone(){
	NeighbourMessage n= new NeighbourMessage(this.label(), this.mark());
	n.setType(this.getType());
	return n;
    }
   
    public String toString(){
        if(this.mark() )
            return "-X-"+this.label();
	return "---"+this.label();
    }
}

    
