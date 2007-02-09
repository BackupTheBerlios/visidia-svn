package visidia.algoRMI;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.simulation.AlgorithmDist;

public class EnvoiMessage2 extends AlgorithmDist {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8870220765335762542L;
	static MessageType labels = new MessageType("labels", true);
    
    public EnvoiMessage2 () {
	this.addMessageType(labels);
    }
    public void init(){

	this.sendTo(0, new StringMessage("Hello",labels));
    }

    public Object clone(){
        return new EnvoiMessage2();
    }
}

