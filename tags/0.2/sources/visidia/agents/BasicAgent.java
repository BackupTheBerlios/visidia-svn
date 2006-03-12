/**
 * This agent moves randomly in the graph.
 */
package visidia.agents;

import visidia.simulation.agents.Agent;

public class BasicAgent extends Agent {

    protected void init() {

        setAgentMover("RandomAgentMover");

        do {
            move();
        } while (1 == 1);
    }
}
