/**
 * Classe  qui sera sous-classee  pour ecrire  les algorithmes  a base
 * d'agents.
 */
public abstract class Agent {
    
  private AgentMover mover;
  private Simulator simulator;

  private static final String defaultAgentMoverName = "LinearAgentMover";

  public Agent (Simulator sim) {
    setMover(defaultAgentMoverName);
  }

  /**
   * Retourne  le nombre  de porte  sortante du  sommet  actuel. Les
   * portes sont rangees de 0 à getArity() - 1.
   */
  public int getArity() { return 0; }

  /**
   * Retourne le numero du sommet en cours. 
   *
   * Attention,  cela  suppose  que  votre  algorithme  utilise  un
   * identifiant unique pour chaque sommet ; ce que vous pourriez ne
   * pas souhaiter.
   */
  public int getVertexIdentity() { return 0; }

  /**
   * Retourne le nombre de sommets du graphe.
   */
  public int getNetSize() { return 0; }

  /**
   * Indique  a  l'agent un  nouveau  type  de deplacement.   <code>
   * agentMoverClass  </code> doit  être une  sous classe  de <code>
   * AgentMover </code>.
   */
  protected   final    void   setMover(String   agentMoverClass)   {
    mover = AgentMover.createAgentMover(agentMoverClass, 
                                        this,
                                        simulator);
  }

  /**
   * Deplace l'agent sur la porte suivante
   */
  public final void move() {
    mover.move();
  }
    
  /**
   * Deplace l'agent sur une porte donne
   */
  public final void move(int door) {
    mover.move(door);
  }
    
  /**
   * Fonction   de  bas   niveau  qui   effectue  le   deplacement  de
   * l'agent. Utiliser plutot move(door).
   */
  public final void moveToDoor(int door) {
    simulator.moveAgentTo(this, door);
  }

  /**
   * Ramene l'agent par la ou il est venu
   */
  protected final void moveBack() {
    mover.move(entryDoor());
  }

  /**
   * Marque la porte passee en parametre avec une certaine marque
   */
  public void setDoorState(MarkedState mark, int door) {}

  /**
   * Retourne le numero de la porte par laquelle on vient d'arriver
   */
  public int entryDoor() { return 0; }

  /**
   * Place une propriete sur le sommet en cours
   */
  public void putVertexProperty(String name, Object value) {}

  /**
   * Retourne la propriete associee a la cle du sommet courant
   */
  public Object getVertexProperty(String name) { return ""; }

  /**
   * Place une propriete sur une porte
   */
  public void putDoorProperty(int door, String name, Object value) {}

  /**
   * Retourne la propriete associee a la cle sur une porte
   */
  public Object getDoorProperty(int door, String name) { return ""; }

  /**
   * Méthode de l'interface Runnable
   */
  public final void run() {/* lance init() */};

  /**
   * Méthode qui spécifie l'action de chaque agent
   */
  protected abstract void init();

  /**
   * Clone l'agent  en cours (avec  son tableau blanc) et  envoie le
   * clone sur la porte en parametre.
   */
  public void cloneAndSend(int door) {}
}
