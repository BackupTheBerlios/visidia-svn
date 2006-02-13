package visidia.simulation;

public class StartSignal {
    Object o;
    public StartSignal(){
	o = new Object();
    }
    public synchronized void waitForStartSignal(){
	try {
	    o.wait();
	}
	catch (InterruptedException e) { }
    }

    public synchronized void go(){
	o.notifyAll();
    }
}
