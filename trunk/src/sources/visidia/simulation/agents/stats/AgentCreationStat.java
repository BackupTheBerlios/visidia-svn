package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class AgentCreationStat extends AbstractAgentStat {

    public AgentCreationStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Created agent";
    }
}
