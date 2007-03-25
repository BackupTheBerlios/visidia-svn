package visidia.simulation.agents.stats;

public class SleepStat extends AbstractAgentStat {

	public SleepStat(Class agClass) {
		super(agClass);
	}

	public SleepStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Sleep time";
	}
}
