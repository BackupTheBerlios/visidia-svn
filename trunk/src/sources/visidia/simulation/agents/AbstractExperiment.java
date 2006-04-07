package visidia.simulation.agents;

import java.util.Map;

public abstract class AbstractExperiment {

    private Map stats;

    public AbstractExperiment() {}

    public void setStats(Map stats) {
        this.stats = stats;
    }

    protected Map getMap() {
        return stats;
    }

    public abstract Map getStats();
}
