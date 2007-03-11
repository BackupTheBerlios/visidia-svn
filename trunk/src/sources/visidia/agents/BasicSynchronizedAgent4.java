package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * I wait 1.88 seconds between each move and I move 4 times before dying.
 * 
 * @see BasicSynchronizedAgent1
 */
public class BasicSynchronizedAgent4 extends SynchronizedAgent {

	protected void init() {

		this.setAgentMover("RandomAgentMover");

		for (int i = 0; i < 4; ++i) {
			this.sleep(1880);
			this.nextPulse();
			this.move();
		}
	}
}
