package visidia.algoRMI;
import visidia.simulation.*;
import visidia.misc.*;

public class EnvoiMessage2 extends AlgorithmDist {
    
    static MessageType labels = new MessageType("labels", true);
    
    public EnvoiMessage2 () {
	addMessageType(labels);
    }
    public void init(){

	sendTo(0, new StringMessage("Hello",labels));
    }

    public Object clone(){
        return new EnvoiMessage2();
    }
}

