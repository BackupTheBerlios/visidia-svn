package visidia.agents;

import java.util.Random;
import java.util.Hashtable;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;


public class RecogniseAgent extends Agent {

    public RecogniseAgent(AgentSimulator sim, Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {

        Random rnd = new Random();
        
        do {
            sleep(1000);

            Integer nbPassages = (Integer) getVertexProperty("nbPassages");
            nbPassages = new Integer(nbPassages.intValue() + 1);
            setVertexProperty("nbPassages", nbPassages);

            System.out.println("Je suis passe " 
                               + nbPassages
                               + " fois pas ce sommet !");
            

            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}
