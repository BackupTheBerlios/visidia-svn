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
    private int step;

    public void init(){

        step = 1;

	while (true) {

	    v = getVertexIdentity();
            labelV = (String)getVertexProperty("label");
	    if (write(v)) {
                step = 1;
                setWB(true);
                randomMove();

		u = getVertexIdentity();
                labelU = (String)getVertexProperty("label");
                door = entryDoor();
                
		if(write(u)) {
                    if(!getWB()) {
                        System.out.print("Handshake success");
                        applyRule();
                    }
		}
                System.out.println(step);
                moveBack();
                System.out.println(step);
                setWB(false);
	    }
	    else {
                System.out.println("Tu ne passeras pas par la!");
                waitForWB(false);
            }
            randomWalk();
            waitMe("nextPulse");
            nextPulse();
        }
    }

    public String toString() {
        switch (step) {
        case 1: return "Construct star";
        case 2: return "No rule";
        case 3: return "Apply rule";
        case 4: return "End application";
        case 5: return "Search new center";
        }
        throw new RuntimeException("This step does not exist!");
    }

    private void applyRule() {
        Star contextStar = contextStar();
        RelabelingSystem rSys = getRelabelling();
        int i = rSys.checkForRule(contextStar);
        waitMe("applyRule");
        if (i == -1) {
            step = 2;
            waitMe("noRule");
        }
        else {
            step = 3;
            waitMe("found rule");
            Rule rule = rSys.getRule(i);
            Star afterStar = rule.after();
            Neighbour neighbourV = afterStar.neighbour(0);
            setVertexProperty("label",neighbourV.state());
            moveBack();
            setVertexProperty("label",afterStar.centerState());
            moveBack();        
            
            if (neighbourV.mark())
                markDoor(door);
            
            step = 4;
        }
    }

    private Star contextStar() {
        Star star = new Star(labelV);
        Neighbour nebV = new Neighbour(labelU);
        star.addNeighbour(nebV);
        return star;
    }

    private void waitForWB(boolean bool) {
        waitMe("WaitForWB");
        while (getWB() != bool) {
            try {
                synchronized (this) {
                    wait(1000);
                } 
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void randomMove() {
        waitMe("randomMove");
	setAgentMover("RandomAgentMover");
	move();
    }

    private void randomWalk() {
        step = 5;
        waitMe("RandomWalk");
	setAgentMover("RandomWalk");
	move();
    }

    private void waitMe(String message) {
        System.out.println("Waiting: " + message + ". " + " step " 
                           + step + ". " + toString());
        try {
            synchronized (this) {
                wait(1);
            } 
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Waiting finished: " + message);
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

