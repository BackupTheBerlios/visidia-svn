package visidia.agents;


import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.FailedMoveStat;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {
	int counter = 0, visited = 1;
    int nbSelectedEdges;
    int nbVertices;
  
    /**
     * Have a look at Spanning_Tree_Agent_WithId#init() for comments.
     */ 
    public void init() {
    	
        nbSelectedEdges = 0;
        nbVertices = this.getNetSize();
        
       //Initialisation des variables qu'on veut afficher dans le white board.
        this.setProperty("nbVertices",(Integer)this.getNetSize() );
        this.setProperty("nbSelectedEdges", nbSelectedEdges);
        
        
        this.setAgentMover("RandomAgentMover");
        this.mark();
        
        while ( nbSelectedEdges < nbVertices - 1 ) {
        	
        	nbSelectedEdges = (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
        	       	
            this.move();
            
            counter = counter ++;
            
            nbVertices = (Integer)this.getWhiteBoard().getValue("nbVertices");
            
            
           
            
            Vertex vertex_A = this.getSimulator().getVertexArrival(this);    
            	
            	if(!vertex_A.getVisualization()){
            	this.processingAgentWhenSwitchingOff();
            	}
            	else
            	{
            		if ( ! this.isMarked() ) {
            		
			                this.markDoor(this.entryDoor());
			                
			                this.mark();
			                this.setProperty("Entry Door for vertex_"+this.getVertexIdentity(),this.entryDoor());
			                this.setProperty("Vertex"+this.getVertexIdentity(),"marked by me");
			                
			                nbSelectedEdges = (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
			                nbSelectedEdges++;
			                this.setProperty("nbSelectedEdges", new Integer(nbSelectedEdges));
            		
            	  }
            		else
            			 this.incrementStat(new FailedMoveStat(this.getClass()));
             }
            
            	nbVertices = (Integer) this.getWhiteBoard().getValue("nbVertices");
                this.setProperty("nbVertices", nbVertices);
            	
            	
            	
            	}
               
            }
            
       
    

    private void mark() {
        //this.setVertexProperty("marked",new Boolean(true) );
        this.setVertexProperty("marked","by "+this );
    }
    //
    private boolean isMarked() {
        boolean mark;

        /**
         * If the vertex is not already marked, an exception is thrown
         * by the WhiteBoard.
         */
        try {
            mark = ((Boolean)this.getVertexProperty("marked")).booleanValue();
        } catch (NoSuchElementException e) {
            mark = false;
        }

        return mark;
    }
}
