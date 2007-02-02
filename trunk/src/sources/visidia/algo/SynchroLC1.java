/**
 * implemente l'algorithme de synchronisation en etoile. 
 */

package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
//import java.util.Random;

public class SynchroLC1 extends Algorithm {
	   
    
    /**
     * algorithme de synchronisation.
     */
    public void init(){
	/*    boolean i = synchroEtoile();
	    if(i==true)
	    setState("centre");
	    else
	    setState("perdu"); 
	*/
	while(true){
	    setState("P"); 
	    if( synchroEtoile() ){
		setState("C");
		
		int n = getArity();
		
		for( int door = 0; door < n; door++){
		    setDoorState(new MarkedState(true),door);
		}
		
		try{
		    Thread.sleep(Math.round(20000 * SynchronizedRandom.nextFloat()));
		}
		catch( InterruptedException e){
		}
		
		for( int door = 0; door < n; door++){
		    setDoorState(new MarkedState(false),door);
		}
	    }
	    
	}
	
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    public boolean synchroEtoile(){
	int arite = getArity() ;
	int[] answer = new int[arite] ;
	
	/*random */
	int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	
	/*Send to all neighbours */
	for(int i=0; i< getArity(); i++){
	sendTo(i,new IntegerMessage(new Integer(choosenNumber)));
	}
	//System.out.println( getId() + "nombre: "+choosenNumber);
	/*receive all numbers from neighbours */
	for( int i = 0; i < arite; i++){
	    Message msg = receiveFrom(i);
	    answer[i]= ((IntegerMessage)msg).value();
	}
	
	/*get the max */
	int max = choosenNumber;
	for( int i=0;i < arite ; i++){
	    if( answer[i] >= max )
		max = answer[i];
	}
	
	/*results */
	return choosenNumber >= max ;
    }

    public String getState(){
	return (String) getProperty("label");
    }

    public void setState(String newState){
	putProperty("label", newState);
    }

    public Object clone(){
	return new SynchroLC1();
    }
}
