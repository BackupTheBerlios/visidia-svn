package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * This agent moves randomly in the graph.
 */
public class BasicSynchronizedAgent extends SynchronizedAgent {

    /**
     * This is the method every agent has to override in order to make
     * it work. When the agent  is started by the simulator, init() is
     * launched.
     */
    protected void init() {

        /**
         * Uses an unpredictable deplacement. It chooses one door randomly.
         */
        setAgentMover("RandomAgentMover");

        do {
            nextPulse();
            move();
        } while (true);
    }
}
