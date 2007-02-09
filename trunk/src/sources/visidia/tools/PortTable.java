package visidia.tools;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import visidia.network.NodeInterfaceTry;


public class PortTable implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 811580916355159223L;
	private Hashtable hash;
    
    public PortTable() {
	this.hash = new Hashtable();
    }

    public PortTable(Hashtable hash) {
	this.hash = hash;
    }

    public int size() {
	return this.hash.size();
    }

    public void put(Integer door, Integer neighbor, NodeInterfaceTry neighborStub){
	Vector v = new Vector();
	v.addElement(neighbor);
	v.addElement(neighborStub);
	this.hash.put(door,v);
    }

    public Integer getNeighbor(Integer door) {
	return (Integer)((Vector)this.hash.get(door)).elementAt(0);
    }

    public int getDoor(Integer neighbor){
	boolean bool = true;
	int i=0;
	for(;(i<this.hash.size()) && bool;i++) {
	    if ((this.getNeighbor(new Integer(i))).equals(neighbor)){
		bool=false;
	    }
	}
	return i-1;
    }

    public NodeInterfaceTry getNeighborStub(Integer door) {
	return (NodeInterfaceTry)((Vector)this.hash.get(door)).elementAt(1);
    }

    public Vector getElement(Integer door) {
	return (Vector)this.hash.get(door);
    }

}
