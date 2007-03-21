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
        
        //On récupère le sommet vers lequel l'agent se dirige
        //pour voir s'il est éteint ou pas.
        
        //Ceux deux lignes sont à revoir avec Ramzy
        //vertex_A.setProperty("NbVisited", 1);
        //this.setProperty("Vertex"+this.getVertexIdentity(),this.getVertexIdentity());
        
        while ( nbSelectedEdges < nbVertices - 1 ) {
        	
        	nbSelectedEdges = (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
        	       	
            this.move();
            
            counter = counter ++;
            nbSelectedEdges = (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
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
			                
			                /*this.setProperty("port_vertex_"+this.getVertexIdentity(),this.entryDoor());
			                this.setProperty("Vertex"+this.getVertexIdentity(),this.getVertexIdentity());*/
			                
			                
			                nbSelectedEdges++;
			                this.setProperty("nbSelectedEdges", new Integer(nbSelectedEdges));
            		
            	  }
            		else
            			 this.incrementStat(new FailedMoveStat(this.getClass()));
             }
            
            	}
               
            }
            
       
    

    private void mark() {
        this.setVertexProperty("marked",new Boolean(true) );
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
