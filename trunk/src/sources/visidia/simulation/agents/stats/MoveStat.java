package visidia.simulation.agents.stats;

public class MoveStat extends AbstractAgentStat {

	public MoveStat(Class agClass) {
		super(agClass);
	}
	
	public MoveStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	
	public String descriptionName() {
		return "Moves";
	}
}
