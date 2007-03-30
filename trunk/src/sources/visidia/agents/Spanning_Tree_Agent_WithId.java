package visidia.agents;

import java.util.Arrays;
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
	                this.setProperty("Entry Door for vertex_"+this.getVertexIdentity() , this.entryDoor());
	                this.setProperty("Vertex"+this.getVertexIdentity(),"marked by me");
	                
	                
	               
                    //Mise à jour de la variable nbSelectedEdges: ceci permet de tenir compte des
	            	//modifications que peut apporter l'utilisateur pendant l'exécution de l'algo.
	            	nbSelectedEdges= (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
	                nbSelectedEdges ++;
	                this.setProperty("nbSelectedEdges", nbSelectedEdges);
	                
	                
	                
	               
	                
	            }
	            else {
	                this.incrementStat(new FailedMoveStat(this.getClass()));
	            }

            nbVertices = (Integer) this.getWhiteBoard().getValue("nbVertices");
            //this.setProperty("nbVertices", nbVertices);
            
            nbSelectedEdges= (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
        }
}

    private void mark (int vertex) {
        this.vertexMarks[vertex] = true;
        this.setVertexProperty("marked","by "+this );
    }

    private boolean isMarked(int vertex) {
        return this.vertexMarks[vertex];
    }

}

