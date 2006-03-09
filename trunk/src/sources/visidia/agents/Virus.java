package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

public class Virus extends Agent {

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
