package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.FailedMoveStat;

/**
 * Implements a spanning tree algorithm with an agent. This agent doesn't use
 * unique identifier of vertices.
 * 
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

	/**
	 * Have a look at Spanning_Tree_Agent_WithId#init() for comments.
	 */
	public void init() {

		int nbSelectedEdges = 0;
		int nbVertices = this.getNetSize();

		this.setAgentMover("RandomAgentMover");

		this.mark();

		while (nbSelectedEdges < nbVertices - 1) {

			this.move();

			if (!this.isMarked()) {
				this.markDoor(this.entryDoor());
				this.mark();
				nbSelectedEdges++;
			} else {
				this.incrementStat(new FailedMoveStat(this.getClass()));
			}
		}
	}

	private void mark() {
		this.setVertexProperty("marked", new Boolean(true));
	}

	private boolean isMarked() {
		boolean mark;

		/**
		 * If the vertex is not already marked, an exception is thrown by the
		 * WhiteBoard.
		 */
		try {
			mark = ((Boolean) this.getVertexProperty("marked")).booleanValue();
		} catch (NoSuchElementException e) {
			mark = false;
		}

		return mark;
	}
}
