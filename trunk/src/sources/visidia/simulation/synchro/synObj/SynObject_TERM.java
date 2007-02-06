package visidia.simulation.synchro.synObj;

import java.io.Serializable;
/**
 * this class contains of implementation concerning Termination 
 */ 
public class SynObject_TERM extends SynObject  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4984757477713462925L;
	/**** Detection of Termination ****/
    protected boolean finishedNode[];
    protected boolean globalEnd;
    protected boolean localEnd;
    
    public SynObject_TERM(){
	super();
	globalEnd = false;
	localEnd = false;
    }
 
    public void init(int ar){
	super.init(ar);
	finishedNode = new boolean[ar];
	for(int i=0;i<ar;i++){
	    finishedNode[i]=false;
	}
    }
    
    public Object clone(){
	return new SynObject_TERM();
    }
    public String toString(){
	return super.toString()+"TERM";
    }
    
    public boolean hasFinished(int neighbour){
	return finishedNode[neighbour];
    }
    public boolean allFinished(){
	return globalEnd;
    }
    public void setGlobEnd(boolean b){
	globalEnd = b;
    }
    public void setFinished(int neighbour, boolean b){
	finishedNode[neighbour] = b;
    }
} 
    
    
    
