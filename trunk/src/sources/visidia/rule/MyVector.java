package visidia.rule;
import java.util.*;
import java.io.Serializable;

/**
 * Randomized methods 
 */

public class MyVector extends Vector implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6068614038815337131L;
	final Vector finalThis = this;
    

    /**
    *
    */   
    public MyVector(){
	super();
    }

    /**
    *
    * @param i
    */   
    public MyVector(int i){
	super(i);
    }
    

    /**
    *
    * @param c
    */   
    public MyVector(Collection c){
	super(c);
    }
    
    /**
    *
    * @return returns the number of elements in the vector.
    */     
    public int count(){
	return size();
    }
    
    /* Neighbours's Methodes */
    
    /**
    *looks in the vector for an elements Neighbour equals to the parameter nei, this   
    * operation is Randomized. 
    * @param nei 
    * @return the index of nei in the vector if exists, else -1 
    */   
    public int indexOf(Neighbour nei)
    {
	Neighbour n = new Neighbour();
	Iterator it = this.randIterator();
	while(it.hasNext()){
	    n = (Neighbour)it.next();
	    if(n.equals(nei))
		return ((Vector) this).indexOf(n);
	}
	return -1;
    }
    
    
    /**
    *looks in the vector for an elements having the same label as the parameter nei.   
    *thia operation is Randomized. 
    *
    * @param nei
    * @return the index of nei in the vector if exists, else -1 
    */   
    public int indexOfLabel(Neighbour nei)
    {
	Neighbour n = new Neighbour();
	Iterator it = this.randIterator();
	while(it.hasNext()){
	    n = (Neighbour)it.next();
	    if(n.sameState(nei))
		return ((Vector) this).indexOf(n);
	}
	return -1;
    }

    
    /* Rules's  Methodes */
    /**
    * clones the vector, all elements should be vectors.
    * no verification is done.
    * @return 
    */   
    public MyVector cloneRules(){
	MyVector v = new MyVector();
	Rule n = new Rule();
	Iterator it = this.iterator();
	while(it.hasNext()){
	    n = (Rule)it.next();
	    v.add(n.clone());
	}
   	return v;
    }
   
    
    /**
     * looks in the vector for an elements Rule equals to the parameter r, this   
     * operation is Randomized. 
     * @param r
     * @return 
     */    
    public int contains(Rule r)
    {
	Rule n = new Rule();
	Iterator it = this.randIterator();
	while(it.hasNext()){
	    n = (Rule)it.next();
	    if(n.equals(r))
		return indexOf(n);
	}
	return -1;
    }


    /**
    *
    * @return a randomIterator for the elements of the vector.
    */   
    public Iterator randIterator() {
	return new Iterator() {
		Vector v = new Vector(finalThis);
		Random r = new Random();
		public boolean hasNext() {
		    return v.size() > 0;
		}
		public Object next() {
		    int pos = r.nextInt(v.size());
		    Object o = v.get(pos);
		    v.remove(pos);
		    return o;
		}
		public void remove() {
		}
	    };
    }
}

