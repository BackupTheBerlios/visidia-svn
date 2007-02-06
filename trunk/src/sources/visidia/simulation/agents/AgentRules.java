package visidia.simulation.agents;

import visidia.simulation.SimulationAbortError;
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

        this.step = 1;

	while (true) {

            if (this.lockVertexIfPossible()) {
                this.v = this.getVertexIdentity();
                this.labelV = (String)this.getVertexProperty("label");
                this.step = 1;

                this.randomMove();

		this.u = this.getVertexIdentity();

                if (this.lockVertexIfPossible()) {
                    this.labelU = (String)this.getVertexProperty("label");
                    this.door = this.entryDoor();
                
                    System.out.print("Handshake success");
                    this.applyRule();
                    this.unlockVertexProperties();
                }

                this.moveBack();
                this.unlockVertexProperties();
	    }
	    else {
                this.waitForWB();
            }
            this.nextPulse();
            this.randomWalk();
        }
    }

    public String toString() {
        switch (this.step) {
        case 1: return "Construct star";
        case 2: return "No rule";
        case 3: return "Apply rule";
        case 4: return "End application";
        case 5: return "Search new center";
        }
        throw new RuntimeException("This step does not exist!");
    }

    private void applyRule() {
        Star contextStar = this.contextStar();
        RelabelingSystem rSys = this.getRelabelling();
        int i = rSys.checkForRule(contextStar);

        if (i == -1) {
            this.step = 2;
        }
        else {
            this.step = 3;
            Rule rule = rSys.getRule(i);
            Star afterStar = rule.after();
            Neighbour neighbourV = afterStar.neighbour(0);
            this.setVertexProperty("label",neighbourV.state());
            this.moveBack();
            this.setVertexProperty("label",afterStar.centerState());
            this.moveBack();        
            
            if (neighbourV.mark())
                this.markDoor(this.door);
            
            this.step = 4;
        }
    }

    private Star contextStar() {
        Star star = new Star(this.labelV);
        Neighbour nebV = new Neighbour(this.labelU);
        star.addNeighbour(nebV);
        return star;
    }

    private void waitForWB() {
        while (this.vertexPropertiesLocked()) {
            try {
                synchronized (this) {
                    this.wait(1000);
                } 
            } catch (InterruptedException e) {
                throw new SimulationAbortError(e);
            }
        }
    }

    private void randomMove() {
	this.setAgentMover("RandomAgentMover");
	this.move();
    }

    private void randomWalk() {
        this.step = 5;
	this.setAgentMover("RandomWalk");
	this.move();
    }
}

