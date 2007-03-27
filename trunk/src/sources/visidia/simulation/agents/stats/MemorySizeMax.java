package visidia.simulation.agents.stats;

public class MemorySizeMax extends AbstractAgentStat {

	public MemorySizeMax(Class agClass) {
		super(agClass);
	}
	
	public MemorySizeMax(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Max of Memory Size";
	}
}
