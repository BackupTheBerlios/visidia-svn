package visidia.agents;

import java.util.Arrays;
import java.util.Hashtable;

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
    
    
    
    
    //Dans cet algorithme, il y a deux variables dont la modification peut simuler
    //des erreurs: ce sont les variables nbSelectedEdges et nbVertices.
    //Pour tenir compte des modifications apporter par l'utilisateur 
    //et donc de simuler des erreurs, il faut stocker et initialiser ces variables
    //dans le whiteboard de l'agent et faire la mise à jour à chaque fois qu'on 
    //les utilise comme c'est expliqué par les commentaires du code.
    protected void init() {
       
        int nbSelectedEdges = 0;
        int nbVertices = this.getNetSize();
		
		//initialisation des variables qu'on veut pouvoir modifier � travers la fenetre du dialogue.
		this.setProperty("nbVertices",(Integer)this.getNetSize() );
        this.setProperty("nbSelectedEdges", nbSelectedEdges);
		
        
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
        while ( nbSelectedEdges < (nbVertices - 1) ) {

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
				
				
				//Mise à jour de la variable pour tenir compte d'une éventuelle modification
				//à partir de la fenetre de dialogue
				nbSelectedEdges= (Integer)this.getProperty("nbSelectedEdges");
                nbSelectedEdges ++;
           
            }
            else {
                this.incrementStat(new FailedMoveStat(this.getClass()));
            }
            
			
			
			//Mise à jour des variables pour tenir compte de modifications apportées par l'algorithme
			this.setProperty("nbSelectedEdges",nbSelectedEdges);
			nbVertices=(Integer)this.getProperty("nbVertices");
        }
    }

    private void mark (int vertex) {
        this.vertexMarks[vertex] = true;
        this.setProperty("Vertex"+vertex, "marked by me");
    }

    private boolean isMarked(int vertex) {
        return this.vertexMarks[vertex];
    }

}
