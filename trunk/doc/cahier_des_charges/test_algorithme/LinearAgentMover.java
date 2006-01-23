import java.util.Arrays;

/**
 * Effectue  un deplacement  lineaire  pour un  agent.  Sur un  sommet
 * donne,  on  va  a  la   premiere  porte  qui  n'a  pas  encore  ete
 * visitee. 
 *
 * Attention, l'implémentation  actuelle tient compte du  fait que les
 * sommets ont un identifiant unique !!!
 */
public class LinearAgentMover extends AgentMover {
    
    int[] nextDoorToGo;

    public LinearAgentMover(Agent ag, Simulator sim) {
        super(ag, sim);
        nextDoorToGo = new int [ag.getNetSize()];

        /* On part de la premiere porte */
        Arrays.fill(nextDoorToGo, 0);
    }

    protected int findNextDoor() {
        int vertex = agent().curVertex();
        int doorToGo = nextDoorToGo[vertex];
        int arity = agent().getArity();

        /* Calcul de la porte suivante */
        nextDoorToGo[vertex] = (nextDoorToGo[vertex] + 1) % arity;

        return doorToGo;
    }
}
