package visidia;

import java.util.Hashtable;

import visidia.graph.*;
import visidia.simulation.Simulator;

public class Main {

    private static SimpleGraph createBasic() {
        SimpleGraph graph = createGraph();

        graph.vertex(new Integer(0)).setData("BasicAgent");

        return graph;
    }

    private static SimpleGraph createSynchronized() {
        SimpleGraph graph = createGraph();

        graph.vertex(new Integer(0)).setData("BasicSynchronizedAgent1");
        graph.vertex(new Integer(1)).setData("BasicSynchronizedAgent1");
        graph.vertex(new Integer(2)).setData("BasicSynchronizedAgent2");
        graph.vertex(new Integer(3)).setData("BasicSynchronizedAgent3");
        graph.vertex(new Integer(4)).setData("BasicSynchronizedAgent4");
        graph.vertex(new Integer(5)).setData("BasicSynchronizedAgent4");

        return graph;
    }

    private static SimpleGraph createRecognise() {
        SimpleGraph graph;
        Hashtable hash = new Hashtable();

        hash.put("nbPassages", new Integer(0));

        graph = createGraph(hash);

        graph.vertex(new Integer(0)).setData("RecogniseAgent");

        return graph;
    }

    private static SimpleGraph createVirus() {
        SimpleGraph graph = createGraph();

        graph.vertex(new Integer(0)).setData("Virus");

        return graph;
    }

    private static SimpleGraph createGraph(Hashtable hash) {
        SimpleGraph graph = new SimpleGraph(hash);

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

    private static SimpleGraph createGraph() {
        return createGraph(new Hashtable());
    }

    public static void main (String []args) {

        SimpleGraph graph = createVirus();
        Simulator sim = new Simulator(graph);
        sim.startSimulation();
    }

}

// Local Variables:
// mode: java
// coding: latin-1
// End:
