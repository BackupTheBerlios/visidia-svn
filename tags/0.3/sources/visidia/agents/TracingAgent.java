/**
 * I'm an  agent which draw a path  on the graph. When  I have visited
 * enough vertices, I go back following the same path and removing the
 * marked edges.
 */
package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Vector;

public class TracingAgent extends Agent {

    protected void init() {
        int nbVertices = getNetSize();
        
        // The trace array  will remember doors to go  when wanting to
        // go back
        int trace[] = new int [nbVertices];

        // Number of vertices visited :  a vertex can be counted twice
        // or more if it is visited twice or more.
        int nbStops = 0;

        // Use a  special mover  to try  no to go  to the  sames edges
        // again and again. The path followed will the be clearer.
        setAgentMover("NoBackMover");

        // I want to visited nbVertices vertices (take care one vertex
        // can be visited more than one time).
        for (nbStops = 0 ; nbStops < nbVertices ; ++nbStops) {
            move();
            markDoor(entryDoor()); // make the last  edge bold to draw
                                   // the path.
            trace[nbStops] = entryDoor(); // remember the door we came
                                          // from.
        }

        // Then go back to the first vertex following exactly the same
        // path and undrawing it.
        for (nbStops = nbStops - 1 ; nbStops >= 0 ; --nbStops) {
            move(trace[nbStops]);
            unmarkDoor(entryDoor()); // remove  the bold state  on the
                                     // edge.
        }
    }
}
