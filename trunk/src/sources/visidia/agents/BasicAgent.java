package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * This agent moves randomly in the graph.
 */
public class BasicAgent extends Agent {

	/**
	 * This is the method every agent has to override in order to make it work.
	 * When the agent is started by the simulator, init() is launched.
	 */
	protected void init() {

		Integer in = new Integer(0);

		/**
		 * Uses an unpredictable deplacement. It chooses one door randomly.
		 */
		this.setAgentMover("RandomAgentMover");

		do {

			try {
				in = (Integer) (this.getVertexProperty("Integer in")) + 1;
			} catch (Exception e) {
				// setVertexProperty("Integer in",in);
			}

			this.setVertexProperty("Integer in", in);

			this.setProperty("test", in);

			this.move();
		} while (true);
	}
}
