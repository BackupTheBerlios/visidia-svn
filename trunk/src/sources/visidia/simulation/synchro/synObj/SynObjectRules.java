package visidia.simulation.synchro.synObj;

import visidia.rule.Star;
import visidia.rule.Neighbour;

import java.io.Serializable;


/*The class mother of Synchronisation objects */

public class SynObjectRules extends SynObject_TERM implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5031963246959857749L;
	public Star neighbourhood = new Star();
    
    /*  Basic */
    
    public String toString(){
	return "SynObjectRules"+super.toString();
    }
    
    public void reset(){
	super.reset();
	neighbourhood.removeAll();
    }
    
    public Object clone(){
	return new SynObjectRules();
    }
    
    /* Neighbourhood Accessors */
    public void setNeighbourhood(Star n){
	neighbourhood = n;
    }
    public void setCenterState(String label){
	neighbourhood.setCenterState(label);
    }
    
    public void refresh(){
	for(int i=0; i < synDoors.size(); i++){
	    int door = ((Integer) synDoors.get(i)).intValue();
	    neighbourhood.addNeighbour(new Neighbour(getMark(door), door));
	}
	
    }
}
    
    
