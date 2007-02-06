package visidia.graph;
/**
 * exception levée lorsqu'un client tente de faire une opération
 * sur un sommet dont l'identité n'existe pase le graphe.
 */
public class NoSuchIdException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9012640320210051671L;

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
