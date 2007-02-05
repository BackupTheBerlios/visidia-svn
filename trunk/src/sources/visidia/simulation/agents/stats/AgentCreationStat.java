package visidia.simulation.agents.stats;

public class AgentCreationStat extends AbstractAgentStat {

    public AgentCreationStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Created agent";
    }
}
