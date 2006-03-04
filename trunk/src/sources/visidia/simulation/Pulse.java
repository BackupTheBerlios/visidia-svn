package visidia.simulation;

/**
 * represente a pulse in synchronous mode. This is just an int wrapper.
 */
public class Pulse {
    private int pulse;
    
    public Pulse(){
    }


    public Pulse(int num){
	pulse = num;
    }


    public int intValue(){
	return pulse;
    }

    public void setValue(int num){
	pulse = num;
    }
}	
