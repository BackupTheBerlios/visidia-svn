package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class TerminatedStat extends AbstractAgentStat {

    public TerminatedStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Terminated algorithms";
    }
}
