package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Remembers how many time an agent modified a vertex whiteboard.
 */
public class VertexWBChangeStat extends AbstractAgentStat {

    public VertexWBChangeStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Vertex WB changes";
    }
}
