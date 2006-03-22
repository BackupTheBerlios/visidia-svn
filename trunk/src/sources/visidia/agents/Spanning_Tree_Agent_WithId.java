package visidia.agents;

import java.util.Arrays;

import visidia.simulation.agents.Agent;


/**
 * Implements a spanning tree algorithm with an agent. This agent uses
 * the unique identification for each vertex.
 *
 * @see Spanning_Tree_Agent_WithoutId
 */
public class Spanning_Tree_Agent_WithId extends Agent {

    /**
     * Remembers if the vertex has already be seen at least once.
     */
    boolean[] vertexMarks;

    protected void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();
        
        setAgentMover("RandomAgentMover");

        vertexMarks = new boolean [nbVertices];

        /**
         * Put false on all cells of vertexMarks.
         */
        Arrays.fill(vertexMarks, false);

        /**
         * Mark the first vertex as already been seen.
         */
        mark(getVertexIdentity());

        /**
         * A tree has nbVertices - 1 edges.
         */
        while ( nbSelectedEdges < (nbVertices - 1) ) {

            move();

            if ( ! isMarked(getVertexIdentity()) ) {
                /**
                 * The current vertex has not been seen already.
                 */

                /**
                 * Put the last  edge in bold. It will  be part of the
                 * tree.
                 */
                markDoor(entryDoor());

                mark(getVertexIdentity());
                nbSelectedEdges ++;
            }
            else {
                incrementStat("Failed moves");
            }

        }
    }

    private void mark (int vertex) {
        vertexMarks[vertex] = true;
    }

    private boolean isMarked(int vertex) {
        return vertexMarks[vertex];
    }

}
