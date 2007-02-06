package visidia.agents;

import visidia.simulation.agents.Agent;


public class ChangeLabelAgent extends Agent {

    protected void init() {
	String label = new String("B");

        this.setAgentMover("RandomAgentMover");

        do {
            this.setVertexProperty("label",label);
            this.move();
        } while (true);
    }
}
