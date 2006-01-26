package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import fr.enserb.das.graph.*;

public  VertexState extends SimpleGraphVertex {
    
    
    protected StringNodeState nodeState;
    
    VertexState(StringNodeState nodeState) {
	nodeState = nodeState;
    }
    
    public void setNodeState(StringNodeState nodeState) {
	nodeState = nodeState;
    }

    public String getNodeState() {
	return nodeState;
    }
}
