package visidia.graph;

import java.util.Enumeration;

/**
* une énumeration d'objets <code>Integer</code>.
*/
public class IntegerEnumeration{
	Enumeration v_enum;

	public IntegerEnumeration(Enumeration v_enum){
	    this.v_enum = v_enum;
	}

	public boolean hasMoreElements(){
		return this.v_enum.hasMoreElements();
	}

	public Integer nextElement(){
		return (Integer) this.v_enum.nextElement();
	}
}
