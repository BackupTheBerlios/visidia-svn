package visidia.simulation.rules;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.*;
//import visidia.simulation.synchro.synObj.*;

//import visidia.simulation.rules.*;
import visidia.rule.*;
//import visidia.misc.*;
//import visidia.simulation.*;
//import java.util.Vector;
 
public class LC2Rule extends AbstractRule {
    
    public LC2Rule() {
	super();
	synType = SynCT.LC2;
	synal = new LC2();
    }
    public LC2Rule(RelabelingSystem r){ 
	super(r);
	synType = SynCT.LC2;
	synal = new LC2();
    }
    
    public Object clone(){
	LC2Rule algo = new LC2Rule();
	algo.copy(this);
	return algo;
    }
    public String toString(){
	return "LC2Rule"+super.toString();    
    } 
}



    	







