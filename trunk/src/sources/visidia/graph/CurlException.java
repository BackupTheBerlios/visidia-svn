package visidia.graph;

/**
 * Exception générée lorsqu'un client tente de creer des 
 * boucles dans le graphe.
 */
public class CurlException extends RuntimeException {
    /**
     * fait appel au constructeur <code>super()</code>
     */
    public   CurlException(){
	super();
    }

    /**
     * fait appel au constructeur <code>super(s)</code>
     */
    public CurlException(String s){
	super(s);
    }
}
