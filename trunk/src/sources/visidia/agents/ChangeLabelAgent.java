package visidia.agents;

import visidia.simulation.agents.Agent;


public class ChangeLabelAgent extends Agent {

    protected void init() {
	String label = new String("B");

        setAgentMover("RandomAgentMover");

        do {
            try{
                getVertexProperty("label");
	    }
	    catch(NoSuchElementException e) {
	    }

            setVertexProperty("label",label);
            move();
        } while (true);
    }
}
