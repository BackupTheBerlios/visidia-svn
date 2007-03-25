package visidia.simulation.agents.stats;

public class MoveStat extends AbstractAgentStat {

	public MoveStat(Class agClass) {
		super(agClass);
	}
	
	public MoveStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Moves";
	}
}
