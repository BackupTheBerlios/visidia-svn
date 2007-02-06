package visidia.graph;
/**
 * exception levée lorsqu'un client tente de faire une opération
 * sur une arête qui n'existe pas dans le graphe.
 */
public class NoSuchLinkException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9147544126565386079L;

	/**
     * fait appel au constructeur <code>super()</code>
     */
    public  NoSuchLinkException(){
	super();
    }

    /**
     * fait appel au constructeur <code>super(s)</code>
     */
    public NoSuchLinkException(String s){
	super(s);
    }
}
