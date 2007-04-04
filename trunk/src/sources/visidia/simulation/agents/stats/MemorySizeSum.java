package visidia.simulation.agents.stats;

public class MemorySizeSum extends AbstractAgentStat {

	public MemorySizeSum(Class agClass) {
		super(agClass);
	}
	
	public MemorySizeSum(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Sum of Memory Size [in WB key(s)]";
	}
}
