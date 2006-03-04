package visidia.graph;

/**
* <p>Cette classe implemente la structue d'un graphe simple.
* Par definition , un graphe simple est non oriente,
* sans boucle et sans aretes multiples. Chaque sommet est represente 
* par un entier qui est unique a travers tout le graphe. 
* 
*/
import java.util.*;

public class SimpleGraphStruct implements Cloneable{
    
    private Hashtable hash;
    
    public SimpleGraphStruct(){
	hash = new Hashtable();	
    }
    
    /**
     * Ajoute un sommet identifie par <i>id</i> au graphe.
     * 
     * @exception AddIdTwiceException levee si une identite est 
     * ajoutee deux fois
     */
    public void put(Integer id){
	if( contains(id) ){
	    throw new AddIdTwiceException(new String("Already contains the id : "+id));
	}
	
	hash.put(new Integer(id.intValue()),new Vector(5,10));
    }
    
    /**
     * Retourne <code>true</code> si le graphe contient 
     * un sommet est identifie par <i>id</id> 
     */
    public boolean contains(Integer id){
	return hash.containsKey(id);
    }
    
    /**
     * retourne retourne le nombre de voisisns du sommet 
     * identifie par <i>id</i>.
     * @exception NoSuchIdException levee si le graphe ne 
     * contient aucun sommet identifie par <i>id</i>.
     */
    public int degree(Integer id){
	return getNeighbourVector(id).size();
    }
    
    /**
     * retourne les identites de tous les sommets du graphe.
     */
    public IntegerEnumeration allId(){
	return new IntegerEnumeration(hash.keys());
    }
    
    /**	
     * lie les deux sommets identifies respectivements par
     * <i>id1</i> et <i>id2</i>.
     *
     * @exception NoSuchIdException levee si le graphe ne 
     * contient pas l'une des identitees <i>id1</i> et <i>id2</i>.
     * @exception CurlException levee si <i>id1</i> et <i>id2</i>
     * identifient le meme sommet. 
     */
    public void link(Integer id1, Integer id2){
	if( id1.equals(id2) ){
	    throw new CurlException(new String(" This graph do not supports curls")); 
	}
	
	Vector v1 = getNeighbourVector(id1);
	Vector v2 = getNeighbourVector(id2); 
	
	if( v1.contains(id2) || v2.contains(id1))
	    return;
	
	v1.add(id2);
	v2.add(id1);
    }
    
    
    /**
     * retourne <code>true</code> si le graphe contient les sommets 
     * identifies respectivements par <i>id1</i> et <i>id2</i>, et s'ils sont
     * lies.
     *
     * @exception NoSuchIdException levee si le graphe ne 
     * contient aucun sommet identifie par <i>id1</i> ou <i>id2</i>.
     */
    public boolean areLinked(Integer id1, Integer id2){ 
	Vector v1 = getNeighbourVector(id1);
	Vector v2 = getNeighbourVector(id2); 
	
	//la premiere condition devrait suffir
	return v1.contains(id2) && v2.contains(id1);		
    }
    
    
    /**
     * retourne les identites des sommets voisins de celui
     * identifie par <i>id</i>.
     * @exception NoSuchIdException levee si le graphe ne 
     * contient aucun sommet identifie par <i>id</i>.
     */
    public IntegerEnumeration neighbours(Integer id){
	return new IntegerEnumeration(getNeighbourVector(id).elements());
    }
    
    /**
     * supprime le sommet identifie par <i>id</i> du graphe 
     * apres avoir eventuellement supprime le(s) lien(s) avec 
     * ses voisins.
     * @exception NoSuchIdException levee si le graphe ne 
     * contient aucun sommet identifie par <i>id</i>.
     */
    public void remove(Integer id){
	IntegerEnumeration neighb = neighbours(id);
	while(neighb.hasMoreElements()){
	    unlink(id,neighb.nextElement());
	}
	hash.remove(id);
    }


    /**	
     * supprime le lien entre les deux sommets identifies respectivements par
     * <i>id1</i> et <i>id2</i>.
     *
     * @exception NoSuchIdException levee si le graphe ne 
     * contient pas l'une des identitees i>id1</i> et <i>id2</i>.
     */
    public void unlink(Integer id1, Integer id2){
	if( ! areLinked(id1,id2) )
	    return;
	
	getNeighbourVector(id1).remove(id2);
	getNeighbourVector(id2).remove(id1);
    }
    
    
    private Vector getNeighbourVector(Integer id){
	if( contains(id) ){
	    throw new NoSuchIdException( new String(" this graph don't contain id "+id));
	}
	return (Vector) hash.get(id);
    }
    
    /**
     * retourne un clone de cet graphe.
     */
    public Object clone(){
	return null;
    }
    
    
    /**
     * affiche le graphe sur la sortie standard pour deboggage.
     */
    public void print(){
	IntegerEnumeration idEnum = allId();
	
	while( idEnum.hasMoreElements() ){
	    Integer id = idEnum.nextElement();
	    System.out.print(id+" --> " );
	    
	    IntegerEnumeration neighbEnum = neighbours(id);
	    while( neighbEnum.hasMoreElements() ){
		System.out.print(neighbEnum.nextElement() + ", ");
	    }
	    
	    System.out.println();
	}
    }
}

		




