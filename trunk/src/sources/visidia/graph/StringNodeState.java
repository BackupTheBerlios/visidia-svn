package visidia.graph;
import visidia.simulation.*;
import visidia.misc.*;
import visidia.graph.*;

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
