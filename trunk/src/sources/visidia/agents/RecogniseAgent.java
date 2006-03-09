package visidia.agents;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;


public class RecogniseAgent extends Agent {

    public RecogniseAgent(AgentSimulator sim, Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {

        setAgentMover("LinearAgentMover");
        
        do {
            sleep(1000);
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
