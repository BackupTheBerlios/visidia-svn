package visidia.simulation;

import visidia.tools.Criterion;

/**
 * DoorCriterion is used to select message packet according to its
 * incomming door.
 */
class DoorCriterion implements Criterion {
    private int wantedDoor;

    DoorCriterion(int wantedDoor){
	this.wantedDoor = wantedDoor;
    }
    
    public boolean isMatchedBy(Object o){
	if( ! (o instanceof MessagePacket) )
	    return false;

	MessagePacket mesgPacket = (MessagePacket) o;
        int door = mesgPacket.receiverDoor();

	return door == this.wantedDoor; 
    }
}
