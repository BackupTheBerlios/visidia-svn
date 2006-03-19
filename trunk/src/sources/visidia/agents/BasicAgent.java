package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * This agent moves randomly in the graph.
 */
public class BasicAgent extends Agent {

    /**
     * This is the method every agent has to override in order to make
     * it work. When the agent  is started by the simulator, init() is
     * launched.
     */
    protected void init() {

        /**
         * Use an unpredictable deplacement. It chooses one door randomly.
         */
        setAgentMover("RandomAgentMover");

        do {
            move();
        } while (true);
    }
}
