package visidia.rule;

import java.io.Serializable;

import visidia.simulation.synchro.SynCT;

/**
 * class Relabeling system options.
 */
public class RSOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3641387447017521217L;

	protected int synAlgo;

	public boolean manageTerm;

	/**
	 * default values are are SynCT.NOT_SPECIFIED as synchronization algorithm,
	 * and true for manage termination.
	 */

	public RSOptions() {
		this(SynCT.NOT_SPECIFIED, true);
	}

	public RSOptions(int syn, boolean term) {
		this.synAlgo = syn;
		this.manageTerm = term;
	}

	public int defaultSynchronisation() {
		return this.synAlgo;
	}

	public String toString() {
		return "Syn=" + this.synAlgo + "Term" + this.manageTerm;
	}

	public Object clone() {
		return new RSOptions(this.synAlgo, this.manageTerm);
	}
}
