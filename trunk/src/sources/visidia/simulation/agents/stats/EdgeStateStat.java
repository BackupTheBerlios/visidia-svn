package visidia.simulation.agents.stats;

public class EdgeStateStat extends AbstractAgentStat {

	public EdgeStateStat(Class agClass) {
		super(agClass);
	}

	public EdgeStateStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Edge state changes";
	}
}
