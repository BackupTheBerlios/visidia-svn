package visidia.simulation.agents.stats;

public class SleepStat extends AbstractAgentStat {

	public SleepStat(Class agClass) {
		super(agClass);
	}

	public SleepStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	
	public String descriptionName() {
		return "Sleep time";
	}
}
