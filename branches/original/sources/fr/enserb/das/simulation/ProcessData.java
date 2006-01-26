package fr.enserb.das.simulation;

import java.util.Hashtable; 
import fr.enserb.das.graph.*;
import fr.enserb.das.tools.*;
import fr.enserb.das.misc.*;

/**
 * Store net nodes data.
 **/
class ProcessData {
    protected Hashtable props;
    protected Thread processThread;
    protected Queue msgQueue;
    protected Algorithm algo;
    protected int lamportClock = 0;
}

