package visidia;

import visidia.graph.*;
import visidia.simulation.Simulator;

public class Main {

    private static SimpleGraph createGraph() {

        SimpleGraph graph = new SimpleGraph();

        graph.put(new Integer(0));
        graph.put(new Integer(1));
        graph.put(new Integer(2));
        graph.put(new Integer(3));
        graph.put(new Integer(4));
        graph.put(new Integer(5));

        graph.link(new Integer(0), new Integer(1));
        graph.link(new Integer(1), new Integer(2));
        graph.link(new Integer(2), new Integer(3));
        graph.link(new Integer(3), new Integer(4));
        graph.link(new Integer(4), new Integer(5));
        graph.link(new Integer(5), new Integer(0));

        graph.link(new Integer(0), new Integer(3));

        return graph;
    }

    public static void main (String []args) {

        SimpleGraph graph = createGraph();
        Simulator sim = new Simulator(graph);
        sim.startSimulation();
    }

}

// Local Variables:
// mode: java
// coding: latin-1
// End:
