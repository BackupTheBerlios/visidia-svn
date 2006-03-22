package visidia.agents.agentsmover;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

import java.util.Arrays;

/**
 * I'm a  linear agent mover which  try not to  go to the door  I just
 * came from.
 */
public class NoBackMover extends LinearAgentMover {
    
    public NoBackMover(Agent ag) {
        super(ag);
    }

    protected int findNextDoor() {
        int doorToGo;

        // Ask the LinearAgentMover for the next door
        doorToGo = super.findNextDoor();

        // Here, the  door to go is  verified not to be  from where we
        // just came from.
        try {
            if (doorToGo == agent().entryDoor())
                doorToGo = super.findNextDoor(); //ask the next door another time
        } catch (IllegalStateException e) {
            // It's normal to get here  for the first vertex (there is
            // no entry door for the first vertex).
        }

        return doorToGo;
    }
}
