package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

public class RecogniseAgent extends Agent {

    protected void init() {

        setAgentMover("LinearAgentMover");
        
        do {
            Integer nbPassages;

            try {

                nbPassages = (Integer) getVertexProperty("nbPassages");
            } catch (NoSuchElementException e) {
                nbPassages = 0;
            }
            nbPassages = new Integer(nbPassages.intValue() + 1);
            setVertexProperty("nbPassages", nbPassages);

            System.out.println(getVertexIdentity() + " has seen an agent "
                               + nbPassages + " time(s).");

            move();
        } while (1 == 1);
    }
}
