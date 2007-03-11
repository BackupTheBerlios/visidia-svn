package visidia.simulation.rules;

import visidia.rule.RelabelingSystem;
import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.RDV;

public class RDVRule extends AbstractRule {

	public RDVRule() {
		super();
		this.synType = SynCT.RDV;
		this.synal = new RDV();
	}

	public RDVRule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.RDV;
		this.synal = new RDV();
	}

	public Object clone() {
		RDVRule algo = new RDVRule();
		algo.copy(this);
		return algo;
	}

	public String toString() {
		return "RDVRule" + super.toString();
	}
}
