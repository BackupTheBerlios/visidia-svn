package visidia.agents;

import java.util.Arrays;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.FailedMoveStat;

/**
 * Implements a spanning tree algorithm with an agent. This agent uses
 * the unique identification for each vertex.
 *
 * @see Spanning_Tree_Agent_WithoutId
 */
public class Spanning_Tree_Agent_WithId extends Agent {

    /**
     * Remembers if the vertex has already been seen at least once.
     */
    
    
     boolean[] vertexMarks;
     
        
     
    
    protected void init() {
       
        int nbSelectedEdges = 0;
        int nbVertices = this.getNetSize();
        
        
        //Initialisation des variables qu'on veut afficher dans
        //le whiteboard de l'agent.
        this.setProperty("nbSelectedEdges",0);
        this.setProperty("nbVertices", nbVertices);
        
        
        this.setAgentMover("RandomAgentMover");
        this.vertexMarks = new boolean [nbVertices];

        /**
         * Puts false on all cells of vertexMarks.
         */
        Arrays.fill(this.vertexMarks, false);

        /**
         * Marks the first vertex as already been seen.
         */
        this.mark(this.getVertexIdentity());

        /**
         * A tree has nbVertices - 1 edges.
         */
        while (nbSelectedEdges < (nbVertices - 1) ) {
        	
        	
        	
            this.move();
            Vertex vertex_A = this.getSimulator().getVertexArrival(this);
            
            if(!vertex_A.getVisualization()){
            	this.processingAgentWhenSwitchingOff();
            	}
            else{
	            if ( ! this.isMarked(this.getVertexIdentity()) ) {
	                /**
	                 * The current vertex has not been seen already.
	                 */
	
	                /**
	                 * Put the last  edge in bold. It will  be part of the
	                 * tree.
	                 */
	                this.markDoor(this.entryDoor());
	
	                this.mark(this.getVertexIdentity());
	                this.setProperty("port_vertex_"+this.getVertexIdentity() , this.entryDoor());
	            
	                nbSelectedEdges= (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
	                nbSelectedEdges ++;
	                this.setProperty("nbSelectedEdges", nbSelectedEdges);
	                
	                
	                
	               
	                nbVertices = (Integer) this.getWhiteBoard().getValue("nbVertices");
	                this.setProperty("nbVertices", nbVertices);
	            }
	            else {
	                this.incrementStat(new FailedMoveStat(this.getClass()));
	            }
            }

        }
    }

    private void mark (int vertex) {
        this.vertexMarks[vertex] = true;
    }

    private boolean isMarked(int vertex) {
        return this.vertexMarks[vertex];
    }

}

