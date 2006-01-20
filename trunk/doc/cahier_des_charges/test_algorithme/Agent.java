/**
 * Classe  qui sera sous-classee  pour ecrire  les algorithmes  a base
 * d'agents.
 */
public abstract class Agent {
    
    /**
     * Retourne  le nombre  de porte  sortante du  sommet  actuel. Les
     * portes sont rangees de 0 à getArity() - 1.
     */
    public int getArity();

    /**
     * Retourne le numero du sommet en cours
     */
    public int curVertex();

    /**
     * Retourne le nombre de sommets du graphe
     */
    public int getNetSize();

    /**
     * Deplace l'agent vers la porte donnee
     */
    public void goToDoor(int door);

    /**
     * Marque la porte passee en parametre avec une certaine marque
     */
    public void setDoorState(MarkedState mark, int door);

    /**
     * Retourne le numero de la porte par laquelle on vient d'arriver
     */
    public int entryDoor();

    /**
     * Place une propriete sur le sommet en cours
     */
    public void putVertexProperty(String key, Object value);

    /**
     * Retourne la propriete associee a la cle du sommet courant
     */
    public Object getVertexProperty(String key);

    /**
     * Place une propriete sur une porte
     */
    public void putDoorProperty(int door, String key, Object value);

    /**
     * Retourne la propriete associee a la cle sur une porte
     */
    public Object getDoorProperty(int door, String key);    
}
