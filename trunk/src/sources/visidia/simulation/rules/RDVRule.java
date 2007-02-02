package visidia.simulation.rules;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.*;
//import visidia.simulation.synchro.synObj.*;

//import visidia.simulation.rules.*;
import visidia.rule.*;
//import visidia.misc.*;
//import visidia.simulation.*;
//import java.util.Vector;
 
public class RDVRule extends AbstractRule{
    
    public RDVRule() {
	super();
	synType = SynCT.RDV;
	synal = new RDV();
    }
    public RDVRule(RelabelingSystem r){ 
	super(r);
	synType = SynCT.RDV;
	synal = new RDV();
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



    	







