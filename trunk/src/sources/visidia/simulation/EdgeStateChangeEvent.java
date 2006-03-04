package visidia.simulation;

import visidia.misc.EdgeState;
import java.io.Serializable;

/**
 * cette classe représente l'évènement associé au changement
 * d'état d'un noeud.
 */
public class EdgeStateChangeEvent implements SimulEvent, Serializable {
    private long evtNum;
    private Integer id1 = null;
    private Integer id2 = null;
    private EdgeState es = null;
 
    /**
     * construit un évènement associé au changement d'état de l'arête 
     *<i>(nodeId1,nodeId2)</i>, lorsqu'il passe à l'état <i>newState</i>.
     */
    public EdgeStateChangeEvent(Long eventNumber, Integer nodeId1, Integer nodeId2, EdgeState newState){
	es = (EdgeState) newState.clone();
	id1 = new Integer(nodeId1.intValue());
	id2 = new Integer(nodeId2.intValue());
	evtNum = eventNumber.longValue();
    }
    
    /**
     * donne le numero de l'évènement.
     */
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * donne le type de l'évènement.
     */
    public int type(){
	return SimulConstants.EDGE_STATE_CHANGE;
    }

    /**
     * donne l'identité du premier extrémité de l'arête qui change d'état.
     */
    public Integer nodeId1(){
	return id1;
    }
    
    /**
     * donne l'identité de la deuxième extrémité de l'arête qui change
     * d'état.
     */
    public Integer nodeId2(){
	return id2;
    }
    
    /**
     * donne le nouvel état de l'arête.
     */
    public EdgeState state(){
	return es;
    }
}
    

