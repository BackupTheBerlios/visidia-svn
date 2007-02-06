package visidia.simulation;

public class StartSignal {
    Object o;
    public StartSignal(){
	this.o = new Object();
    }
    public synchronized void waitForStartSignal(){
	try {
	    this.o.wait();
	}
	catch (InterruptedException e) { }
    }

    public synchronized void go(){
	this.o.notifyAll();
    }
}
