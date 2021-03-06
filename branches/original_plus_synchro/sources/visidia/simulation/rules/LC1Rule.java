package visidia.simulation.rules;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.*;
import visidia.simulation.synchro.synObj.*;

import visidia.simulation.rules.*;
import visidia.rule.*;
import visidia.misc.*;
import visidia.simulation.*;
import java.util.Vector;
 
public class LC1Rule extends AbstractRule{

    public LC1Rule() {
	super();
	synType = SynCT.LC1;
	synal = new LC1();
    }
    public LC1Rule(RelabelingSystem r){ 
	super(r);
	synType = SynCT.LC1;
	synal = new LC1();
    }
    
    public Object clone(){
	LC1Rule algo = new LC1Rule();
	algo.copy(this);
	return algo;
    }
    public String toString(){
	return ("RSAlgo: synal="+this.synType+" opt="+relSys.userPreferences.toString()+"\n RS="+relSys.toString());    
    }
    /* for LC1 */
    public void sendMyState(){
	for(int i=0; i<  synob.getCenters().size(); i++) {
	    int door = ((Integer)  synob.getCenters().elementAt(i)).intValue();
	    if (synob.isConnected(door)) {
		sendTo(door,new StringMessage(((String) getProperty("label")),MSG_TYPES.LABE));
	    }
	}
    }
    /* for LC1 */
    public void receiveAndUpdateMyState(){
	for(int i=0;i<  synob.getCenters().size();i++){
	    int neighbour = ((Integer) synob.getCenters().elementAt(i)).intValue();
	    if(synob.isConnected(neighbour)){
		Message msg = receiveFrom(neighbour);
		if(msg != null){
		    setDoorState(new MarkedState(((BooleanMessage)msg).data()), neighbour);
		    synob.setMark(neighbour,((BooleanMessage)msg).data());
		}
	    }
	}
    }
}


