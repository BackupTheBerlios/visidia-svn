package visidia.simulation.agents.stats;

public class EdgeStateStat extends AbstractAgentStat {

	public EdgeStateStat(Class agClass) {
		super(agClass);
	}

	public EdgeStateStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	
	public String descriptionName() {
		return "Edge state changes";
	}
}
