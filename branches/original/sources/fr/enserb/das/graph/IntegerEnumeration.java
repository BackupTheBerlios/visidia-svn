package fr.enserb.das.graph;

import java.util.Enumeration;

/**
* une enumeration d'objets <code>Integer</code>.
*/
public class IntegerEnumeration{
	Enumeration enum;

	public IntegerEnumeration(Enumeration enum){
	    this.enum = enum;
	}

	public boolean hasMoreElements(){
		return enum.hasMoreElements();
	}

	public Integer nextElement(){
		return (Integer) enum.nextElement();
	}
}
