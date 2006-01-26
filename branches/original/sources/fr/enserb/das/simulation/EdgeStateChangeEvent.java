package fr.enserb.das.simulation;

import fr.enserb.das.misc.EdgeState;
import java.io.Serializable;

/**
 * cette classe represente l'evenement associe au changement
 * d'etat d'un noeud.
 */
public class EdgeStateChangeEvent implements SimulEvent, Serializable {
    private long evtNum;
    private Integer id1 = null;
    private Integer id2 = null;
    private EdgeState es = null;
 
    /**
     * construit un evenement associe au changement d'etat de l'arete 
     *<i>(nodeId1,nodeId2)</i>, lorsqu'il passe a l'etat <i>newState</i>.
     */
    public EdgeStateChangeEvent(Long eventNumber, Integer nodeId1, Integer nodeId2, EdgeState newState){
	es = (EdgeState) newState.clone();
	id1 = new Integer(nodeId1.intValue());
	id2 = new Integer(nodeId2.intValue());
	evtNum = eventNumber.longValue();
    }
    
    /**
     * donne le numero de l'evenement.
     */
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * donne le type de l'evenement.
     */
    public int type(){
	return SimulConstants.EDGE_STATE_CHANGE;
    }

    /**
     * donne l'identite du premier extremite de l'arete qui change d'etat.
     */
    public Integer nodeId1(){
	return id1;
    }
    
    /**
     * donne l'identite du deuxieme extremite de l'arete qui change d'etat.
     */
    public Integer nodeId2(){
	return id2;
    }
    
    /**
     * donne le nouvel etat de l'arete.
     */
    public EdgeState state(){
	return es;
    }
}
    

