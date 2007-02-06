package visidia.algoRMI;
import visidia.simulation.*;
import visidia.misc.*;

public class EnvoiMessage2 extends AlgorithmDist {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8870220765335762542L;
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

