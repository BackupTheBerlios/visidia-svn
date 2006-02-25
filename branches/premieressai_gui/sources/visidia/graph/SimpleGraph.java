package visidia.graph;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Stack;
import java.io.*;
import visidia.visidiassert.VisidiaAssertion;

/**
 * Cette classe implemente une une structure de graphe
 * par liste de successeurs. Chaque noeud est represente par
 * un nombre entier unique.
 */
public class SimpleGraph implements Cloneable, Serializable{
    /* liste des noeuds */
    private Hashtable hash;

    private Hashtable defaultVertexValues = null;
    
    /**
     *Construit un nouveau graphe simple vide.
     */	
    public SimpleGraph(){
        this(null);
    }
    
    public SimpleGraph(Hashtable defaultVertexValues) {
	hash = new Hashtable();
        this.defaultVertexValues = defaultVertexValues;
    }

    /**
     * Ajoute un sommet identifie par <i>id</i> au graphe.
     * 
     * @exception AddIdTwiceException levee si l'identite <i>id</i> existe
     * deja� dans le graphe.
     */	
    public void put(Integer id){
	//System.out.println(id);
	if( contains(id) ){
	    System.out.println(id);
	    throw new AddIdTwiceException();
	}
	
        hash.put(id , new SimpleGraphVertex(id, defaultVertexValues));
    }
    
    
    /**
     * retourne une enumration de tous les sommets du graphe. L'enumeration retournée
     * contient des Objets de type <code>Vertex</code>.
     */	
    public Enumeration vertices(){
	return hash.elements();
    }
    


    /**
     *Retourne le sommet correspondant à l'identité id.
     */	
    public Vertex vertex(Integer id){
	Vertex v = getSimpleGraphVertex(id);
	
	if( v == null ){
	    throw new NoSuchIdException();
	}

	return v;
    }
    
    public Vertex nextVertex(Integer id) {
	Vertex v = vertex(vertex(id).getNext());
	return v;
    }
    
    /**
     * retourne le nombre de sommets du graphe.
     */	
    public int size(){
	return hash.size();
    }
    
    /**
     * lie les deux sommets identifiés respectivements par
     * <i>id1</i> et <i>id2</i>. S'il existe une arête entre
     * les deux sommets cette méthode ne fait rien. 
     * @exception NoSuchIdException levee si le graphe ne 
     * contient pas l'une des identitées <i>id1</i> et <i>id2</i>.
     * @exception CurlException levée si <i>id1</i> et <i>id2</i>
     * identifient le même sommet. 
     */	
    public void link(Integer id1, Integer id2){
	SimpleGraphVertex sgv1 = getSimpleGraphVertex(id1);
	SimpleGraphVertex sgv2 = getSimpleGraphVertex(id2);
	
	SimpleGraphEdge edg_12 = new SimpleGraphEdge(this,sgv1, sgv2); // arete de sgv1 vers sgv2
	SimpleGraphEdge edg_21 = new SimpleGraphEdge(this,sgv2, sgv1); // arete de sgv2 vers sgv1
	sgv1.addNeighbour(sgv2,edg_12);
	sgv2.addNeighbour(sgv1,edg_21);
    }
   
    /* nouvelle méthode pour positionner le suivant si l'arête est orienté*/

    public void orientedLink(Integer id1, Integer id2) {
	link(id1, id2);
	setNextVertex(id1,id2);
	setPreviousVertex(id1, id2);
    }
    /**
     *   positionne le suivant du sommet id1 à id2 
     */

    public void setNextVertex(Integer id1, Integer id2) {
	Vertex sgv1 = vertex(id1);

	sgv1.setNext(id2);
    }
    
    public void setPreviousVertex(Integer id1, Integer id2) {
	Vertex sgv2 = vertex(id2);
	sgv2.setPrevious(id1);
    }
    /**
     * retourne l'arête liant les deux sommets identifiés
     * respectivements par <i>id1</i> et <i>id2</i>.
     * @exception NoSuchIdException levée si le graphe ne 
     * contient pas l'une des identitées <i>id1</i> et <i>id2</i>.
     * @exception NoSuchLinkException levee s'il n'y a pas d'arête entre 
     * les deux sommets identifiés respectivements par
     * <i>id1</i> et <i>id2</i>.
     */
    public Edge edge(Integer id1, Integer id2){
	return vertex(id1).edge(id2);
    }
    
    /**
     * supprime le lien entre les deux sommets identifiés respectivements par
     * <i>id1</i> et <i>id2</i>. S'il n'existe pas une arête entre
     * les deux sommets cette méthode ne fait rien. 
     * @exception NoSuchIdException levée si le graphe ne 
     * contient pas l'une des identitées <i>id1</i> et <i>id2</i>.
     * @exception CurlException levée si <i>id1</i> et <i>id2</i>
     * identifient le même sommet. 
     *
     */	
    public void unlink(Integer id1, Integer id2){
	if( ! areLinked(id1,id2) ){
	    return; // si l'arête n'existe pas, ne rien faire.
	}

	SimpleGraphVertex sgv1 = getSimpleGraphVertex(id1);
	SimpleGraphVertex sgv2 = getSimpleGraphVertex(id2);
	sgv1.removeNeighbour(sgv2);
	sgv2.removeNeighbour(sgv1);
    }
    
    
    /**
     * retourne vrai si les sommets identifiés respectivements par
     * <i>id1</i> et <i>id2</i> sont liés par une arête.
     * @exception NoSuchIdException levée si le graphe ne 
     * contient pas l'une des identitées <i>id1</i> et <i>id2</i>.
     * @exception CurlException levée si <i>id1</i> et <i>id2</i>
     * identifient le même sommet. 
     */
    public boolean areLinked(Integer id1, Integer id2){
	Vertex v1 = vertex(id1);
	Vertex v2 = vertex(id2);

	if( id1.equals(id2) ){
	    throw new CurlException();
	}
	
	return v1.isNeighbour(id2) && v2.isNeighbour(id1);
    }
    
    
    /**
     * Supprime le sommet identifié par <i>id</i>, Les arêtes partants de
     * ce sommet sont supprimés d'abord. 
     * @exception NoSuchIdException levée si le graphe ne 
     * contient pas un sommet identifiée par id.
     */	
    public void remove(Integer id){
	SimpleGraphVertex sgv = getSimpleGraphVertex(id);
	Enumeration e = sgv.neighbours();
	
	/* supprime tous les arêtes sortants du sommet */

	/* empile les élements à supprimer.
	* une suppression directe détruit la structure
	* de l'enumeration.
	*/
	Stack neighbStack = new Stack();
	while( e.hasMoreElements() ){
		neighbStack.push(e.nextElement());
	}

	while( ! neighbStack.isEmpty() ){
	    SimpleGraphVertex sgv1 = (SimpleGraphVertex) neighbStack.pop();
	    unlink(sgv.identity(),sgv1.identity());
	}
	
	/*
	  while( e.hasMoreElements() ){
	  SimpleGraphVertex sgv1 = (SimpleGraphVertex) e.nextElement();
	  unlink(sgv.identity(),sgv1.identity());
	  System.out.println("*********");
	  }
	*/	
	hash.remove(sgv.identity());
    }
    
    
    
    /**
     * retourne <code>true</code> si ce graphe contient un sommet identifié
     * par <i>id</i>.
     */	
    public boolean contains(Integer id){
	try{
	   getSimpleGraphVertex(id);
	}
	catch(NoSuchIdException e){
	    return false;
	}
	
	return true;
    }
    

    /*
     *
     */	
    private SimpleGraphVertex getSimpleGraphVertex(Integer id){
	SimpleGraphVertex sgv = (SimpleGraphVertex) hash.get(id);
	if(sgv == null){
	    throw new NoSuchIdException();
	}

	return sgv;
    }
    
    


    /**
     * crée un clone de ce graphe. Ceci ne duplique que la structure du graphe, les objets
     * référencés par les sommets et les arêtes ne sont pas dupliqués.
     */
    public Object clone(){
	SimpleGraph sg = new SimpleGraph();
	
	/* clonage des somments */ 
	Enumeration vEnum = vertices();
	while( vEnum.hasMoreElements() ){
	    Vertex vtx = (Vertex) vEnum.nextElement();
	    sg.put(vtx.identity());
	    sg.vertex(vtx.identity()).setData(vtx.getData());
	    sg.vertex(vtx.identity()).setNext(vtx.getNext());
	    sg.vertex(vtx.identity()).setPrevious(vtx.getPrevious());
	}
	
	/* clonage des arêtes entre les sommets */
	vEnum = vertices();
	while( vEnum.hasMoreElements() ){
	    Vertex vtx = (Vertex) vEnum.nextElement();
	    SimpleGraphVertex cloneVtx = (SimpleGraphVertex) sg.vertex(vtx.identity());
	    Enumeration succEnum = vtx.neighbours();

	    while( succEnum.hasMoreElements() ){
		Vertex vtxSucc = (Vertex) succEnum.nextElement();
		SimpleGraphVertex cloneVtxSucc = (SimpleGraphVertex) sg.vertex(vtxSucc.identity());
		SimpleGraphEdge edg = new SimpleGraphEdge(sg,cloneVtx,cloneVtxSucc);
		//SimpleGraphEdge edg = new SimpleGraphEdge(sg,cloneVtxSucc,cloneVtx);
		cloneVtx.addNeighbour(cloneVtxSucc,edg);
		//cloneVtxSucc.addNeighbour(cloneVtx,edg);
	    }
	    
	}
	
	
	return sg;
    }

    
    public void print(){
	Enumeration e = hash.elements();
	while( e.hasMoreElements() ){
	    SimpleGraphVertex sgv = (SimpleGraphVertex) e.nextElement();
	    sgv.print();
	}
    }
}

















