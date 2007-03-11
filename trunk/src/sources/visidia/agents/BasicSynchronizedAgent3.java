package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * I wait 0.3 second between each move and I move 4 times before dying.
 * 
 * @see BasicSynchronizedAgent1
 */
public class BasicSynchronizedAgent3 extends SynchronizedAgent {

	protected void init() {

		this.setAgentMover("RandomAgentMover");

		for (int i = 0; i < 4; ++i) {
			this.sleep(300);
			this.nextPulse();
			this.move();
		}
	}
}
