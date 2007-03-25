package visidia.simulation.agents.stats;

public class AgentCreationStat extends AbstractAgentStat {

	public AgentCreationStat(Class agClass) {
		super(agClass);
	}
	public AgentCreationStat(Class agClass, String agName) {
		super(agClass,agName);
	}
	
	
	public String descriptionName() {
		return "Created agent";
	}
}
