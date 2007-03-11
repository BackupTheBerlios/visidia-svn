package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Agent that remembers how many times it visited each vertex.
 * 
 * @see Agent#setVertexProperty(Object, Object)
 * @see Agent#getVertexProperty(Object)
 */
public class RecogniseAgent extends Agent {

	protected void init() {

		this.setAgentMover("LinearAgentMover");

		do {
			Integer nbPassages;

			/**
			 * When the whiteboard does not contain an element, it throws a
			 * NoSuchElementException.
			 */
			try {
				nbPassages = (Integer) this.getVertexProperty("nbPassages");
			} catch (NoSuchElementException e) {
				nbPassages = 0;
			}

			nbPassages = new Integer(nbPassages.intValue() + 1);
			this.setVertexProperty("nbPassages", nbPassages);

			System.out.println(this.getVertexIdentity() + " has seen an agent "
					+ nbPassages + " time(s).");

			this.move();
		} while (true);
	}
}
