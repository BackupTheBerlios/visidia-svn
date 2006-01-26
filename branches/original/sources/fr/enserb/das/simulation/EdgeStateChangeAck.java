package fr.enserb.das.simulation;

import java.io.Serializable;

/**
 * elle est utilisee pour informer le systeme de simulation 
 * que l'evenement correspondant a un envoie de message
 * a ete prise en compte.
 */
public class EdgeStateChangeAck implements  SimulAck, Serializable{
    private long num;
    
    public EdgeStateChangeAck(Long evtNumber){
	num = evtNumber.longValue();
    }

    public Long number(){
	return new Long(num);
    }

    public int type(){
	return SimulConstants.EDGE_STATE_CHANGE;
    }
}
