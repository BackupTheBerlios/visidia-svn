package fr.enserb.das.graph;
/**
 * exception levee lorsqu'un client tente d'ajouter un sommet dont
 * l'identite existe deja dans le graphe.
 */
public class AddIdTwiceException extends RuntimeException {
    /**
     * fait appel au constructeur <code>super()</code>
     */
    public AddIdTwiceException(){
	super();
    }

    /**
     * fait appel au constructeur <code>super(s)</code>
     */
    public AddIdTwiceException(String s){
	super(s);
    }
}
