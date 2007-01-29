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

    /**
     * Implements the  spanning tree algorithm. This  algorithm uses a
     * boolean array indexed by the vertices which is initialised with
     * <code>false</code>.   Every time  the agent  sees a  vertex not
     * marked (the associated boolean in the array is false), it marks
     * it (put  the boolean to true)  and mark the edge  he came from.
     * <p>
     *
     * The agent moves randomly in the graph.
     */
    protected void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();
        
        setAgentMover("RandomAgentMover");

        vertexMarks = new boolean [nbVertices];

        /**
         * Puts false on all cells of vertexMarks.
         */
        Arrays.fill(vertexMarks, false);

        /**
         * Marks the first vertex as already been seen.
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
		/**
		 * Last move did not help building the tree. Count it.
		 */
                incrementStat(new FailedMoveStat(this.getClass()));
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
