package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class MoveStat extends AbstractAgentStat {

    public MoveStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Moves";
    }
}
