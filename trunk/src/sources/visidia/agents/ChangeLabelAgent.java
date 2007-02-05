package visidia.agents;

import visidia.simulation.agents.Agent;


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
