package visidia.simulation.agents.stats;

public class FailedMoveStat extends AbstractAgentStat {

	public FailedMoveStat(Class agClass) {
		super(agClass);
	}
	
	public FailedMoveStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	public String descriptionName() {
		return "Failed moves";
	}
}
