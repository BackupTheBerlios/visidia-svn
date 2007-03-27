package visidia.simulation.agents.stats;

public class MemoryAverageSize extends AbstractAgentStat {

	public MemoryAverageSize(Class agClass) {
		super(agClass);
	}
	
	public MemoryAverageSize(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Memory Average Size";
	}
}
