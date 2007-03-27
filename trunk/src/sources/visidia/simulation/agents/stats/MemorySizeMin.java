package visidia.simulation.agents.stats;

public class MemorySizeMin extends AbstractAgentStat {

	public MemorySizeMin(Class agClass) {
		super(agClass);
	}
	
	public MemorySizeMin(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Min of Memory Size";
	}
}
