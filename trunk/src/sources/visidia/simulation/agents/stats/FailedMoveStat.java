package visidia.simulation.agents.stats;

public class FailedMoveStat extends AbstractAgentStat {

	public FailedMoveStat(Class agClass) {
		super(agClass);
	}
	
	public FailedMoveStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	public String descriptionName() {
		return "Failed moves";
	}
}
