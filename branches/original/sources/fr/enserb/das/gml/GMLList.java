package fr.enserb.das.gml;

import java.util.*;
/**
 * Set of pair (key, value). The containt of this set can be queried
 * by label.
 */
public class GMLList {
	Hashtable hash = new Hashtable();
	
	
	/**
	 */
	public Enumeration getValues(String key){
		Vector vect = (Vector) hash.get(key);
		if(vect != null){
			return vect.elements();
		}
		
		return new EmptyEnumeration();
	}
	
	public Object getValue(String key){
		Vector vect = (Vector) hash.get(key);
		if(vect != null){
			return vect.get(0);
		}
		
		return null;
	}
	
	/**
	 */
	public void add(String key, Object value){
		if(hash.containsKey(key)){
			((Vector) hash.get(key)).add(value);
		}
		else{
			Vector vect = new Vector(1,5);
			vect.add(value);
			hash.put(key,vect);
		}
	
	}
}

class EmptyEnumeration implements Enumeration{
    public boolean hasMoreElements(){
	return false;
    }

    public Object nextElement(){
	throw new NoSuchElementException("empty enumeration");
    }
}
