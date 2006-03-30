package visidia.simulation.agents;

import java.util.NoSuchElementException;

import visidia.misc.MarkedState;

import visidia.rule.Neighbour;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.rule.Star;


public class AgentRules extends AbstractAgentsRules {

    private int v,u;
    private String labelV, labelU;
    private int door;
    
    public void init(){

	while (true) {
	    v = getVertexIdentity();
            labelV = (String)getVertexProperty("label");
	    if (write(v)) {
                setWB(true);
                randomMove();
		u = getVertexIdentity();
                labelU = (String)getVertexProperty("label");
                door = entryDoor();
                
		if(write(u)) {
                    if(!getWB()) {
			System.out.println("Handshake success");
                        applyRule();
                    }
		}
		moveBack();
                setWB(false);
	    }
	    else {
                waitForWB(false);
            }
            randomWalk();
            nextPulse();
        }
    }

    private void applyRule() {
        Star contextStar = contextStar();
        RelabelingSystem rSys = getRelabelling();
        int i = rSys.checkForRule(contextStar);
        if (i == -1)
            return;
        Rule rule = rSys.getRule(i);
        Star afterStar = rule.after();
        setVertexProperty("label",afterStar.centerState());
        moveBack();
        Neighbour neighbourV = afterStar.neighbour(0);
        setVertexProperty("label",neighbourV.state());
        int doorNum = neighbourV.doorNum();
        if (neighbourV.mark())
            markDoor(doorNum);

        moveBack();            
    }

    private Star contextStar() {
        Star star = new Star(labelU);
        Neighbour nebV = new Neighbour(labelV);
        star.addNeighbour(nebV);
        return star;
    }

    private void waitForWB(boolean bool) {
        while (getWB() != bool) {
            try {
                synchronized (this) {
                    wait(1000);
                } 
            } catch (InterruptedException e) {
            }
        }
    }

    private void randomMove() {
	setAgentMover("RandomAgentMover");
	move();
    }

    private void randomWalk() {
	setAgentMover("RandomWalk");
	move();
    }

    private void setWB(boolean bool) {
        setVertexProperty("wb",new Boolean(bool));
    }
   
    private boolean getWB() {
        boolean bool;
        try {
            bool = ((Boolean)getVertexProperty("wb")).booleanValue();
        } catch (NoSuchElementException e) {
            setWB(false);
            bool = false;
        }
        return bool;
    }
}

