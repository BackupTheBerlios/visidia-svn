package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * 
 */
public class Akka extends SynchronizedAgent {

	protected void init() {
		Random rand = new Random();
		while (true) {
			int i = rand.nextInt(this.getArity() + 1);
			if (i < this.getArity())
				this.moveToDoor(i);

			this.nextPulse();

			this.lockVertexProperties();
			Integer tmp = (Integer) this.getVertexProperty("nb");
			tmp++;

			this.setVertexProperty("nb", tmp);
			this.unlockVertexProperties();

			this.nextPulse();

		}
	}
}
