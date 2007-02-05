package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;


/**
 * Implements a Handshake algroithm
 * 
 */
public class Handshake extends SynchronizedAgent {


    protected void init() {
	setAgentMover("RandomAgentMover");	
	
	boolean wbMarkedByMe = false;
	boolean wbNeighMarkedByMe = false;
	
	while ( true ) {
	    try{
		wbMarkedByMe = false;
		wbNeighMarkedByMe = false;

		/**
		 * l'agent essaye de marquer le noeud courant
		 **/
		lockVertexProperties();
		if(((String)getVertexProperty("label")).equals("N")) {
		    setVertexProperty("label", new String("M"));
		    wbMarkedByMe = true;
		} 
		unlockVertexProperties();
		
		nextPulse();
		
		/**
		 * celui qui réussit à marquer le noeud courant se
		 * déplace vers un nouveau sommet et essaye de marquer
		 * ce dernier. Les autres attendent.
		 */
		if(wbMarkedByMe) {
		 
		    move();
		    nextPulse();
		    
		    lockVertexProperties();
		    if(((String)getVertexProperty("label")).equals("N")) {
			setVertexProperty("label",new String("M"));
			wbNeighMarkedByMe = true;
			markDoor(entryDoor());
			syncDoor(entryDoor());
			try{
			    Thread.sleep(1000);
			} catch (Exception e) {}
		    } 
		    unlockVertexProperties();
		    nextPulse();
		    
		    if(wbNeighMarkedByMe) {
			setVertexProperty("label",new String("N"));
		    }
		    
		    moveToDoor(entryDoor());
		    nextPulse();
		    
		    if(wbNeighMarkedByMe) {
			unsyncDoor(entryDoor());
			unmarkDoor(entryDoor());
		    }
		    setVertexProperty("label",new String("N"));
		    
		} else {
		    
		    nextPulse();
		    
		    nextPulse();
		    
		    nextPulse();
		    
		}
		
		move();
		nextPulse();
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
