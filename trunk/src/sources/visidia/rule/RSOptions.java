package visidia.rule;
import java.io.Serializable;
import visidia.simulation.synchro.SynCT;
/**
 * class Relabeling system options.
 */
public class RSOptions implements Serializable {
    
    protected int synAlgo;

    public boolean manageTerm;
    /**
     * default values are are SynCT.NOT_SPECIFIED as synchronization
     * algorithm, and true for manage termination.
     */ 
    
    public RSOptions() {
	this(SynCT.NOT_SPECIFIED, true);
    }
    
    public RSOptions(int syn, boolean term) {
	synAlgo=syn;
	manageTerm = term;
    }
    
    public int defaultSynchronisation() {
	return this.synAlgo; 
    }
    
    public String toString() {
	return "Syn="+synAlgo+"Term"+manageTerm;
    }
    
    public Object clone() {
	return new RSOptions(synAlgo, manageTerm);
    }
}
