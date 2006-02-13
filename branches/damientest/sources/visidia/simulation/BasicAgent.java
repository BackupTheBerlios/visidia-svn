package visidia.simulation;

import java.util.Random;

public class BasicAgent extends Agent {

    public BasicAgent(Simulator sim) {
        super(sim);
    }

    protected void init() {

        Random rnd = new Random();

        do {
            sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}
