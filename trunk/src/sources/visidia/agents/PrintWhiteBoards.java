package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Set;
import java.util.Iterator;

/**
 * This agent moves  randomly in the graph and  prints the whiteboards
 * it meets on the terminal.
 *
 * @see visidia.tools.agents.WhiteBoard
 */
public class PrintWhiteBoards extends Agent {

    protected void init() {

        setAgentMover("RandomAgentMover");

        do {

            Set keys = getVertexPropertyKeys();
            Iterator it = keys.iterator();

            System.out.println("On vertex " + getVertexIdentity() + ":");

            while(it.hasNext()) {
                Object key = it.next();

                System.out.println(key + ": " + getVertexProperty(key));
            }

            move();

        } while (true);
    }
}
