package visidia.simulation.agents.stats;

public class VertexWBChangeStat extends AbstractAgentStat {

	public VertexWBChangeStat(Class agClass) {
		super(agClass);
	}
	
	public VertexWBChangeStat(Class agClass, Integer agId) {
		super(agClass, agId);
	}
	
	public String descriptionName() {
		return "Vertex WB changes";
	}
}
