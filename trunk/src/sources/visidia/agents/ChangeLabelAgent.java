package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * I'm an  agent that  simply changes the  label of each  vertex which
 * label is not B.
 */
public class ChangeLabelAgent extends Agent {

    protected void init() {
	String label = new String("B");

        setAgentMover("RandomAgentMover");

        do {
            setVertexProperty("label",label);
            move();
        } while (true);
    }
}
