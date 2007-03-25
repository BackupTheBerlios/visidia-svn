package visidia.simulation.agents.stats;

public class VertexWBChangeStat extends AbstractAgentStat {

	public VertexWBChangeStat(Class agClass) {
		super(agClass);
	}
	
	public VertexWBChangeStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	
	public String descriptionName() {
		return "Vertex WB changes";
	}
}
