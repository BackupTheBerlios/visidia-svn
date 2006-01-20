/**
 * Effectue  un deplacement  lineaire  pour un  agent.  Sur un  sommet
 * donne, on va a la premiere porte qui n'a pas encore ete visitee.
 */
public class LinearAgentMover extends AgentMover {
    
    int[] nextDoorToGo;

    public LinearAgentMover(Agent ag) {
        super(ag);
        nextDoorToGo = new int [ag.getNetSize()];

        /* On part de la premiere porte */
        Arrays.fill(nextDoorToGo, 0);
    }

    int findNextDoor() {
        int vertex = agent().curVertex();
        int doorToGo = nextDoorToGo[vertex];
        int arity = agent().getArity();

        /* Calcul de la porte suivante */
        nextDoorToGo[vertex] = (nextDoorToGo[vertex] + 1) % arity;

        return nextDoor;
    }

}
