package visidia.simulation.agents.stats;

public class MaxNbAgent extends AbstractAgentStat {

	public MaxNbAgent(Class agClass) {
		super(agClass);
	}
	
	public MaxNbAgent(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Max Number of Agents";
	}
}
