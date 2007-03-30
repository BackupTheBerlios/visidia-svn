package visidia.agents;


import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;
import visidia.simulation.agents.stats.FailedMoveStat;
import java.util.LinkedList;
import java.util.Iterator;
import visidia.simulation.agents.MoveException;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_WithoutId extends Spanning_Tree_Agent {

    
    public void init() {
    	
        this.setAgentMover("RandomAgentMover");
        AgentMover am = this.getAgentMover();
        
        //Initialisation des variables qu'on veut afficher dans le white board.
        this.setProperty("nbVertices",Integer.valueOf(this.getNetSize()));
        this.setProperty("nbSelectedEdges", new Integer(0));
        
        // Initialisation du sommet de départ
        this.setVertexIdTree(this.getIdentity());
        
        while ( (Integer)this.getProperty("nbSelectedEdges") < (Integer)this.getProperty("nbVertices") - 1 ) {
        	
        	System.out.println("nbSelectedEdges" + ((Integer)this.getProperty("nbSelectedEdges")).toString());
        	System.out.println("nbVertices" + ((Integer)this.getProperty("nbVertices")).toString());
        	/*
        	 * DEPART VERTEX
        	 */
        	
            // Choose the next door
        	try {
        		this.setProperty("ExitPort", new Integer(am.findNextDoor()));
        	}
        	catch(MoveException e) {
        		this.processingAgentWhenSwitchingOff();
        	}
        	
        	boolean newDoor = this.getVertexPort(this.getIdentity(), (Integer)this.getProperty("ExitPort")) == null;
        	
        	// Set the door as a Child
        	if(newDoor)
        		this.setVertexPortToChild(this.getIdentity(), (Integer)this.getProperty("ExitPort"));
        	
        	// Move to the choosen door
            this.move((Integer)this.getProperty("ExitPort"));

            
        	/*
        	 * ARRIVAL VERTEX
        	 */
            
    		this.setProperty("EntryPort", new Integer(this.entryDoor()));
    
    		
            //Vertex vertex_A = this.getSimulator().getVertexArrival(this);

    		
			if (!this.isVertexBelongToTheTree(this.getIdentity())) {
			
				System.out.println("add");
				this.setVertexIdTree(this.getIdentity());
				this.setVertexPortToParent(this.getIdentity(), (Integer)this.getProperty("EntryPort"));
				
				// Coloration graphique
				this.markDoor(this.entryDoor());
			
				this.setProperty("nbSelectedEdges", (Integer)this.getProperty("nbSelectedEdges") +1);
			
			}
			else if(!newDoor) {
			}
			else {
				// The vertex is already member of the tree
				System.out.println("notaddmoveback");
				
				// Move back
				//this.move((Integer)this.getProperty("EntryPort"));
				this.moveBack();
				
				this.delVertexPort(this.getIdentity(), (Integer)this.getProperty("EntryPort"));

				System.out.println("notaddmoveback3");
}
			
			System.out.println("fininit()");

		}

	}
            


    
}
