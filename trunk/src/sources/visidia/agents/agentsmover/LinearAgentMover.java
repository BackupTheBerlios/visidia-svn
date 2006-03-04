package visidia.agentsmover;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

import java.util.Arrays;

/**
 * Provide a linear move for an Agent. On a vertex, the agent go to
 * the first never-visited door.
 *
 * /!\ Warning, this implementation imply that the vertex have an unique
 * identifier !!!
 */
public class LinearAgentMover extends AgentMover {
    
    int[] nextDoorToGo;

    public LinearAgentMover(Agent ag, AgentSimulator sim) {
        super(ag, sim);
        nextDoorToGo = new int [ag.getNetSize()];

        /* On part de la premiere porte */
        Arrays.fill(nextDoorToGo, 0);
    }

    protected int findNextDoor() {
        int vertex = agent().getVertexIdentity();
        int doorToGo = nextDoorToGo[vertex];
        int arity = agent().getArity();

        /* Calcul de la porte suivante */
        nextDoorToGo[vertex] = (nextDoorToGo[vertex] + 1) % arity;

        return doorToGo;
    }
}
