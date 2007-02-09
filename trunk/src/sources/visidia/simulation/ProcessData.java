package visidia.simulation;

import java.util.Hashtable;

import visidia.tools.VQueue;

/**
 * Store net nodes data.
 **/
class ProcessData {
    protected Hashtable props;
    protected Thread processThread;
    protected VQueue msgVQueue;
    protected Algorithm algo;
    protected int lamportClock = 0;
}

