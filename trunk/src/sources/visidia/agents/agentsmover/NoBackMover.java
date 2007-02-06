package visidia.agents.agentsmover;

import visidia.simulation.agents.Agent;

/**
 * I'm a  linear agent mover which  try not to  go to the door  I have
 * just come from.
 */
public class NoBackMover extends LinearAgentMover {
    
    public NoBackMover(Agent ag) {
        super(ag);
    }

    protected int findNextDoor() {
        int doorToGo;

        // Asks the LinearAgentMover for the next door
        doorToGo = super.findNextDoor();

        // Here, the door  to go is checked not to be  the one we have
        // just come from.
        try {
            if (doorToGo == this.agent().entryDoor())
                doorToGo = super.findNextDoor(); //ask the next door another time
        } catch (IllegalStateException e) {
            // It's normal to get here  for the first vertex (there is
            // no entry door for the first vertex).
        }

        return doorToGo;
    }
}
