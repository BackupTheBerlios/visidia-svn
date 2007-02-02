package visidia.simulation.agents.stats;

//import visidia.simulation.agents.Agent;

public class FailedMoveStat extends AbstractAgentStat {

    public FailedMoveStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Failed moves";
    }
}
