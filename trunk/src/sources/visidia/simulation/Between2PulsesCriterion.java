package visidia.simulation;

import visidia.tools.Criterion;

/**
 * This class is used in the synchronous model. The user can create a
 * criterion and setting an upper an lower bound on the message he
 * searchs in the message queue of a node. If the queue contains any
 * messagePacket, say mp, which have been sent between the two
 * specified pulses, then mp is matched (return true in the
 * isMatchedBy(mp). If door is specified, the mp must arrives from
 * specified door; otherwise it is ignored.
 * 
 */

public class Between2PulsesCriterion implements Criterion {
    
    private int pulseSup;
    private int pulseInf;

    private Door door;
    

    /**
     * no bound given for the max an min pulses, i.e, +infinity and
     * -inifinity
     */
    public Between2PulsesCriterion() {
	pulseSup = -1;
	pulseInf = -1;
	door = null;
    }
    
    /**
     * creates a new criterion with the specified lower and upper bound for pulses
     * @param pulseSup upper bound
     * @param pulseInf lower bound
     *
     **/
    public Between2PulsesCriterion(int pulseSup, int pulseInf) {
	door = null;
	this.pulseSup = pulseSup;
	this.pulseInf = pulseInf;
    }

    /**
     * creates a new criterion with the specified lower and upper bound for pulses
     * @param pulseSup upper bound
     * @param pulseInf lower bound
     * @param door door number
     *
     **/
    
    public Between2PulsesCriterion(int pulseSup, int pulseInf, int door) {
	this.pulseSup =  pulseSup;
	this.pulseInf = pulseInf;
	this.door = new Door(door);
    }

    
    /**
     * See the general description of the class
     * 
     **/
    public boolean isMatchedBy(Object o){
	if ( !(o instanceof MessagePacket)) 
	    return false;

	return true;
	
	//TO DO

	/*
	if(pulseInf == -1 && pulseSup == -1 && door == null) {
	    return true;
	} else if(door == null) {
	    Message msg = ((MessagePacket)o).message();
	    int d = ((MessagePacket)o).receiverDoor();
	    if(door.getNum() == d)
		return true;
	    else 
		return false; 
	} else if (door == null) {
	    Message msg = ((MessagePacket)o).message();
	    int p = msg.getMsgClock();
	    if(pulse.intValue() == p)
		return true;
	    return false; 
	} else {
	    Message msg = ((MessagePacket)o).message();
	    int p = msg.getMsgClock();
	    int d = ((MessagePacket)o).receiverDoor();
	    if(door.getNum() == d && pulse.intValue() == p)
		return true;
	    return false; 
	}
	*/
    }
    
    public int getPulseSup() {
	return pulseSup;
    }
    
    public int getPulseInf() {
	return pulseInf;
    }

    public int getDoor() {
	if (door == null)
	    return -1;
	return door.getNum();
    }

    public void setDoor(int door) {
	if(this.door == null)
	    this.door = new Door(door);
	else
	    this.door.setNum(door);
    }
    
    public void setPulseInf(int pulseInf) {
	this.pulseInf = pulseInf;
    } 

    public void setPulseSup(int pulseSup) {
	this.pulseSup = pulseSup;
    } 
    
    public boolean doorIsNull() {
	return door == null;
    }
    
}
