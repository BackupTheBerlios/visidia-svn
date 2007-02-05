package visidia.simulation.agents.stats;

public class FailedMoveStat extends AbstractAgentStat {

    public FailedMoveStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Failed moves";
    }
}
