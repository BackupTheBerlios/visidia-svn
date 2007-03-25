package visidia.simulation.agents.stats;

public class VertexWBAccessStat extends AbstractAgentStat {

	public VertexWBAccessStat(Class agClass) {
		super(agClass);
	}
	
	public VertexWBAccessStat(Class agClass, String agName) {
		super(agClass, agName);
	}
	
	public String descriptionName() {
		return "Vertex WB access";
	}
}
