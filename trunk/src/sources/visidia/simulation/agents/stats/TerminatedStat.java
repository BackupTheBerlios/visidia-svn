package visidia.simulation.agents.stats;

public class TerminatedStat extends AbstractAgentStat {

    public TerminatedStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Terminated algorithms";
    }
}
