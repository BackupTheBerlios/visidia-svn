package fr.enserb.das.graph;
/**
 * exception levee lorsqu'un client tente de faire une operation
 * sur un sommet dont l'identite n'existe pase le graphe.
 */
public class NoSuchIdException extends RuntimeException {
    /**
     * fait appel au constructeur <code>super()</code>
     */
    public  NoSuchIdException(){
	super();
    }

    /**
     * fait appel au constructeur <code>super(s)</code>
     */
    public NoSuchIdException(String s){
	super(s);
    }
}
