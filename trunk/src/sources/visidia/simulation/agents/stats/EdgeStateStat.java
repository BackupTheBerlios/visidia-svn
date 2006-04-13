package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Implements an event used when the simulator changes an edge state.
 */
public class EdgeStateStat extends AbstractAgentStat {

    public EdgeStateStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Edge state changes";
    }
}
