package visidia.algo;
import visidia.misc.StringMessage;
import visidia.simulation.Algorithm;

public class EnvoiMessage extends Algorithm {

    public void init(){

	for(int i = 1; i<=1000000 ; i++){
	    this.sendTo(0, new StringMessage("Hello"));
	    try{
		Thread.sleep(600);
	    } catch (Exception e) {
	    }
	}
    }
    
    public Object clone(){
        return new EnvoiMessage();
    }
}
