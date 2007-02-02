package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class VertexWBChangeStat extends AbstractAgentStat {

    public VertexWBChangeStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Vertex WB changes";
    }
}
