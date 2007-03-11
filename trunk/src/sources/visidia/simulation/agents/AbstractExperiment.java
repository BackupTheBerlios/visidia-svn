package visidia.simulation.agents;

import visidia.tools.Bag;

public abstract class AbstractExperiment {

	private Bag stats;

	public AbstractExperiment() {
	}

	public void setStats(Bag stats) {
		this.stats = stats;
	}

	protected Bag getBag() {
		return this.stats;
	}

	public abstract Bag getStats();
}
