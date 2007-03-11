package visidia.simulation.rules;

import visidia.rule.RelabelingSystem;
import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.LC2;

public class LC2Rule extends AbstractRule {

	public LC2Rule() {
		super();
		this.synType = SynCT.LC2;
		this.synal = new LC2();
	}

	public LC2Rule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.LC2;
		this.synal = new LC2();
	}

	public Object clone() {
		LC2Rule algo = new LC2Rule();
		algo.copy(this);
		return algo;
	}

	public String toString() {
		return "LC2Rule" + super.toString();
	}
}
