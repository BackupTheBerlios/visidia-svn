package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.Agent;

public class Cop extends Agent {

	static boolean thiefCaptured = false;

	protected void init() {
		Random randMove = new Random();

		while (!Cop.thiefCaptured) {
			int degree = this.getArity();
			int randomDirection = Math.abs(randMove.nextInt(degree));

			this.moveToDoor(randomDirection);

			this.lockVertexProperties();
			if (((String) this.getVertexProperty("label")).equals("S")) {
				Cop.thiefCaptured = true;
				this.setVertexProperty("label", new String("C"));
			}
			this.unlockVertexProperties();
		}
	}
}
