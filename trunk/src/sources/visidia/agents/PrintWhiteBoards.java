package visidia.agents;

import java.util.Iterator;
import java.util.Set;

import visidia.simulation.agents.Agent;

/**
 * This agent moves  randomly in the graph and  prints the whiteboards
 * it mets.
 *
 * @see visidia.tools.agents.WhiteBoard
 */
public class PrintWhiteBoards extends Agent {

    protected void init() {

        this.setAgentMover("RandomAgentMover");

        do {

            Set keys = this.getVertexPropertyKeys();
            Iterator it = keys.iterator();

            System.out.println("On vertex " + this.getVertexIdentity() + ":");

            while(it.hasNext()) {
                Object key = it.next();

                System.out.println(key + ": " + this.getVertexProperty(key));
            }

            this.move();

        } while (true);
    }
}
