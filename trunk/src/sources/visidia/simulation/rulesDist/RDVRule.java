
package visidia.simulation.rulesDist;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgosDist.*;
import visidia.simulation.synchro.synObj.*;

import visidia.simulation.rulesDist.*;
import visidia.rule.*;
import visidia.misc.*;
import visidia.simulation.*;
import java.util.Vector;



public class RDVRule extends AbstractRuleDist{
    
    public RDVRule() {
	super();
	synType = SynCT.RDV;
	synal = new RDV();
	addMessageType(MSG_TYPES.MARK);
    }
    public RDVRule(RelabelingSystem r){ 
	super(r);
	synType = SynCT.RDV;
	synal = new RDV();
	addMessageType(MSG_TYPES.MARK);
    }
    
    public Object clone(){
	RDVRule algo = new RDVRule();
	algo.copy(this);
	return algo;
    }
    public String toString(){
	return "RDVRule"+super.toString();    
    } 

}
