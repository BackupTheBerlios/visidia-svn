package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;


/**
 * Implements a Handshake algroithm
 * 
 */
public class Handshake extends SynchronizedAgent {


    protected void init() {
	this.setAgentMover("RandomAgentMover");	
	
	boolean wbMarkedByMe = false;
	boolean wbNeighMarkedByMe = false;
	
	while ( true ) {
	    try{
		wbMarkedByMe = false;
		wbNeighMarkedByMe = false;

		/**
		 * l'agent essaye de marquer le noeud courant
		 **/
		this.lockVertexProperties();
		if(((String)this.getVertexProperty("label")).equals("N")) {
		    this.setVertexProperty("label", new String("M"));
		    wbMarkedByMe = true;
		} 
		this.unlockVertexProperties();
		
		this.nextPulse();
		
		/**
		 * celui qui réussit à marquer le noeud courant se
		 * déplace vers un nouveau sommet et essaye de marquer
		 * ce dernier. Les autres attendent.
		 */
		if(wbMarkedByMe) {
		 
		    this.move();
		    this.nextPulse();
		    
		    this.lockVertexProperties();
		    if(((String)this.getVertexProperty("label")).equals("N")) {
			this.setVertexProperty("label",new String("M"));
			wbNeighMarkedByMe = true;
			this.markDoor(this.entryDoor());
			this.syncDoor(this.entryDoor());
			try{
			    Thread.sleep(1000);
			} catch (Exception e) {}
		    } 
		    this.unlockVertexProperties();
		    this.nextPulse();
		    
		    if(wbNeighMarkedByMe) {
			this.setVertexProperty("label",new String("N"));
		    }
		    
		    this.moveToDoor(this.entryDoor());
		    this.nextPulse();
		    
		    if(wbNeighMarkedByMe) {
			this.unsyncDoor(this.entryDoor());
			this.unmarkDoor(this.entryDoor());
		    }
		    this.setVertexProperty("label",new String("N"));
		    
		} else {
		    
		    this.nextPulse();
		    
		    this.nextPulse();
		    
		    this.nextPulse();
		    
		}
		
		this.move();
		this.nextPulse();
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
