package visidia.agents;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;

public class Virus extends Agent {

    public Virus(AgentSimulator sim, Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {

        try {
            getVertexProperty("dejaPasse");
        } catch (NoSuchElementException e) {
            setVertexProperty("dejaPasse", this);

            System.out.println("Sommet " + getVertexIdentity()
                               + " conquis par l'agent "
                               + getIdentity() + " !");

            for(int i = 0; i < getArity(); ++i)
                cloneAndSend(i);
        }
    }
}
