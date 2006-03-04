package visidia.tools;

import java.util.*;
import visidia.network.*;
import java.io.Serializable;



public class LocalNodeTable implements Serializable {
    
    private Hashtable hash ;
    
    public LocalNodeTable() {
	hash = new Hashtable();
    }

    public void addLocalNode(String host, String localNode, Integer node){
	try {
	    if (hash.containsKey(host)){
		Hashtable h = (Hashtable)hash.get(host);
		Vector v = (Vector)h.get(localNode);
		if (v == null) {
		    v = new Vector();
		    v.addElement(node);
		    h.put(localNode,v);
		} else {
		    v.addElement(node);
		    h.put(localNode,v);
		}
		hash.put(host,h);
	    } else {
		Hashtable h = new Hashtable();
		Vector v = new Vector();
		v.addElement(node);
		h.put(localNode,v);
		hash.put(host,h);
	    }
	} catch (Exception e) {
	    System.out.println(e);
	}
    }
    
    public void addLocalNode(String host, String localNode, Vector nodes){
	try {
	    if (hash.containsKey(host)){
		Hashtable h = (Hashtable)hash.get(host);
		Vector v = (Vector)h.get(localNode);
		if (v==null) {
		    v = nodes;
		    h.put(localNode,v);
		} else {
		    while (!nodes.isEmpty()) {
			v.addElement((Integer)nodes.remove(0));
			h.put(localNode,v);
		    }
		}
		hash.put(host,h);
		//((Hashtable)hash.get(host)).put(localNode,nodes);
	    } else {
		Hashtable h = new Hashtable();
		h.put(localNode,nodes);
		hash.put(host,h);
	    }
	} catch (Exception e) {
	    System.out.println(e);
	}
    }

    
    public void addToLocalNode(String host, Integer node) {
	try {
	    Hashtable h = (Hashtable)hash.get(host);
	    Enumeration e = h.keys();
	    String localNode = (String) e.nextElement();
	    addLocalNode(host,localNode,node);
	} catch (Exception expt) {
	    System.out.println(expt);
	}
    }
    
	    
    
    public boolean containsHost(String host) {
	return hash.containsKey(host);
    }
    
    
    public Hashtable content() {
	return hash;
    }
    
    public void print() {
	System.out.println("################ BEGIN PRINT TABLE ###############"); 
	Enumeration e = hash.keys();
	while(e.hasMoreElements()){
	    String host = (String)e.nextElement();
	    System.out.println("############ HOST = "+host+" ############"); 
	    Hashtable h = (Hashtable)hash.get(host);
	    Enumeration localNodes = h.keys();
	    while(localNodes.hasMoreElements()){
		String localNode = (String)localNodes.nextElement();
		System.out.print("LocalNode : "+localNode+" --> ");
		Vector v = (Vector)h.get(localNode);
		if(!v.isEmpty()){
		    for(int i=0;i<v.size();i++)
			System.out.print((Integer)v.elementAt(i)+" | ");
		}
		System.out.print("\n");
	    }
	    System.out.print("\n");
	}
	System.out.println("################ END PRINT TABLE ###############");
    }
}
