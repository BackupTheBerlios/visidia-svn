package visidia.simulation.agents.stats;

public class VertexWBAccessStat extends AbstractAgentStat {

	public VertexWBAccessStat(Class agClass) {
		super(agClass);
	}
	
	public VertexWBAccessStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Vertex WB access";
	}
}
