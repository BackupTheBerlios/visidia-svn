package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import visidia.graph.*;

public class VertexState extends SimpleGraphVertex {
    
    
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
