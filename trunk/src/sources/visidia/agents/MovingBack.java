/**
 * Agents to  verify moveBack() is working.  It simply move  to a door
 * and then back to the previous vertex.
 */

package visidia.agents;

import visidia.simulation.agents.Agent;

public class MovingBack extends Agent {

    protected void init() {

	for(int i = 0; i < getArity(); ++i) {
	    moveToDoor(i);
	    moveBack();
            markEntryDoor(); // put the edge in bold
	}
    }
}
