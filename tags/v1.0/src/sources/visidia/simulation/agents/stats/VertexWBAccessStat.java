package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Remembers how many times an agent accessed a vertex whiteboard.
 */
public class VertexWBAccessStat extends AbstractAgentStat {

    public VertexWBAccessStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Vertex WB access";
    }
}
