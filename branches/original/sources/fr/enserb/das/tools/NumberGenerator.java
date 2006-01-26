package fr.enserb.das.tools;

/**
 * A number generator is used to manage the long number space.
 * An allocated number will not be returned by <code>alloc()</code> until
 * it is freed by <code>free()</code> method.
 */
public class NumberGenerator {
    private long n = 0;
    public synchronized long alloc(){
	return n++;
    }

    public synchronized void free(long num){
	// not yet implemented
    }
}
