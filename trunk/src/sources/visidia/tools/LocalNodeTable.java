package visidia.tools;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



public class LocalNodeTable implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1212519964884224960L;
	private Hashtable hash ;
    
    public LocalNodeTable() {
	this.hash = new Hashtable();
    }

    public void addLocalNode(String host, String localNode, Integer node){
	try {
	    if (this.hash.containsKey(host)){
		Hashtable h = (Hashtable)this.hash.get(host);
		Vector v = (Vector)h.get(localNode);
		if (v == null) {
		    v = new Vector();
		    v.addElement(node);
		    h.put(localNode,v);
		} else {
		    v.addElement(node);
		    h.put(localNode,v);
		}
		this.hash.put(host,h);
	    } else {
		Hashtable h = new Hashtable();
		Vector v = new Vector();
		v.addElement(node);
		h.put(localNode,v);
		this.hash.put(host,h);
	    }
	} catch (Exception e) {
	    System.out.println(e);
	}
    }
    
    public void addLocalNode(String host, String localNode, Vector nodes){
	try {
	    if (this.hash.containsKey(host)){
		Hashtable h = (Hashtable)this.hash.get(host);
		Vector v = (Vector)h.get(localNode);
		if (v==null) {
		    v = nodes;
		    h.put(localNode,v);
		} else {
		    while (!nodes.isEmpty()) {
			v.addElement(nodes.remove(0));
			h.put(localNode,v);
		    }
		}
		this.hash.put(host,h);
		//((Hashtable)hash.get(host)).put(localNode,nodes);
	    } else {
		Hashtable h = new Hashtable();
		h.put(localNode,nodes);
		this.hash.put(host,h);
	    }
	} catch (Exception e) {
	    System.out.println(e);
	}
    }

    
    public void addToLocalNode(String host, Integer node) {
	try {
	    Hashtable h = (Hashtable)this.hash.get(host);
	    Enumeration e = h.keys();
	    String localNode = (String) e.nextElement();
	    this.addLocalNode(host,localNode,node);
	} catch (Exception expt) {
	    System.out.println(expt);
	}
    }
    
	    
    
    public boolean containsHost(String host) {
	return this.hash.containsKey(host);
    }
    
    
    public Hashtable content() {
	return this.hash;
    }
    
    public void print() {
	System.out.println("################ BEGIN PRINT TABLE ###############"); 
	Enumeration e = this.hash.keys();
	while(e.hasMoreElements()){
	    String host = (String)e.nextElement();
	    System.out.println("############ HOST = "+host+" ############"); 
	    Hashtable h = (Hashtable)this.hash.get(host);
	    Enumeration localNodes = h.keys();
	    while(localNodes.hasMoreElements()){
		String localNode = (String)localNodes.nextElement();
		System.out.print("LocalNode : "+localNode+" --> ");
		Vector v = (Vector)h.get(localNode);
		if(!v.isEmpty()){
		    for(int i=0;i<v.size();i++)
			System.out.print(v.elementAt(i)+" | ");
		}
		System.out.print("\n");
	    }
	    System.out.print("\n");
	}
	System.out.println("################ END PRINT TABLE ###############");
    }
}
