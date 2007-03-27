package visidia.simulation.agents.stats;

public class MoveMaxStat extends AbstractAgentStat {

	public MoveMaxStat(Class agClass) {
		super(agClass);
	}
	
	public MoveMaxStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Max Step";
	}
}
