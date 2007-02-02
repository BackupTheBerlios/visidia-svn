package visidia.simulation.agents;

import visidia.tools.Bag;

import visidia.simulation.agents.stats.*;

public abstract class AbstractExperiment {

    private Bag stats;

    public AbstractExperiment() {}

    public void setStats(Bag stats) {
        this.stats = stats;
    }

    protected Bag getBag() {
        return stats;
    }

    public abstract Bag getStats();
}
