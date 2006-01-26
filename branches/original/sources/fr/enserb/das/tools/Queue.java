package fr.enserb.das.tools;


import java.util.LinkedList;
import java.util.ListIterator;
import java.util.*;

/**
 * A Queue object implements a FIFO (First In First Out) list. It can be used in multi-threads
 * environment since all it's methods are synchronized. The get (resp. put) method blocks when
 * there is no element to return (resp. when the queue contains maxSize elements). If the calling
 * thread is interrupted while it is blocked in these methods, the <code>InterruptedException</code>
 * is thrown.  
 */
public class Queue{
	/**
	* taille maximale de la file d'attente.
	*/
	private int maxSize;  

	/* La structure stockant la file de messages*/
	private LinkedList queue;

	/**
	 * Constructs a queue with default maximum size which is Integer.MAX_VALUE.
	 */
	public Queue(){
		this(Integer.MAX_VALUE);
	}

	/**
	*  Constructs a queue with maximum size <code>maxSize</code>
	*/
	public Queue(int maxSize){
		this.maxSize = maxSize;
		queue = new LinkedList();
	}

	/**
	* Return the first element in the queue. if the queue is empty, this method block
	* until an element is available.
	*/
	public synchronized Object get() throws InterruptedException{
		while( queue.isEmpty() ){
			wait();
		}
		notifyAll();
		return queue.removeFirst();
	}

	/**
	 * Return the first element in the queue that match the criterion <code>c</code>.
	 * If the queue does not contains any element that match the criterion <code>c</code>,
	 * this method block until one that macth <code>c</code> be available. 
	 */
	public synchronized Object get(Criterion c) throws InterruptedException{
		while(true){
			ListIterator li = queue.listIterator();
			while( li.hasNext() ){
				Object o = li.next();
				if( c.isMatchedBy(o) ){
				    li.remove();
				    return o;
				}
			}
			wait();
		}
	}
    
    /** 
     * Add one element a the end of the queue. If the queue contanis <code>maxSize</code>
     * elements this method block until one element be consumed.
     */
    public synchronized void put(Object obj)throws InterruptedException{
	while( queue.size() >= maxSize ){
	    wait();
	}
	queue.addLast(obj);
	notifyAll();
    }
    
    /**
	 * Return true if the queue is empty.
	 */
    public synchronized boolean isEmpty(){
	return queue.isEmpty();
	}
    
    /**
     * Returns true if the queue contains one element that match the criterion c.
     */
    public synchronized boolean contains(Criterion c){
		ListIterator li = queue.listIterator();
		while( li.hasNext() ){
			Object o = li.next();
			if( c.isMatchedBy(o) ){
				return true;
			}
		}
		return false;
	}	

	/**
	 * remove all elements from the queue.
	 */
	public synchronized void purge(){
		queue = new LinkedList();
	}
	
	/**
	 * return this queue maximum size.
	 */
	public int getMaxSize(){
		return maxSize;
	}
}
