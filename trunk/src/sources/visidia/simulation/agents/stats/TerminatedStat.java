package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Remembers how many algorithms finished their work.
 */
public class TerminatedStat extends AbstractAgentStat {

    public TerminatedStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Terminated algorithms";
    }
}
