package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Provides an  event used when  a new agent  is created. Since  I'm a
 * subclass  of AbstractAgentStat, I  store the  agent class  that was
 * created.
 */
public class AgentCreationStat extends AbstractAgentStat {

    public AgentCreationStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Created agent";
    }
}
