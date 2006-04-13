package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Implements   an  event   used   when  an   agent   has  moved   for
 * nothing.  Agents producing spanning  trees use  me to  remember how
 * many times they move and did not mark any new edge.
 *
 * @see visidia.agents.Spanning_Tree_Agent_WithId
 * @see visidia.agents.Spanning_Tree_Agent_WithoutId
 */
public class FailedMoveStat extends AbstractAgentStat {

    public FailedMoveStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Failed moves";
    }
}
