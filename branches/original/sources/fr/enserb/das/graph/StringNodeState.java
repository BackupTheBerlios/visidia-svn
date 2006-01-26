package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import fr.enserb.das.graph.*;

public class StringNodeState  {
    
    
    protected String state;
    
    StringNodeState(String s) {
	state = s;
    }
    
    public void setString(String s) {
	state = s;
    }

    public String getString() {
	return state;
    }
}
