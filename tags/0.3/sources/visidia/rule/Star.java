package visidia.rule;

import java.io.*;
import java.util.*;
import java.lang.*;

import visidia.rule.*;
import java.io.Serializable;


/**
 *
 */
public class Star implements Serializable {
    
    protected String centerState;
    protected MyVector neighbourhood;
    static String unknown = "UNKNOWN";
    /**
     * default constructor.   
     * default centerState is "UNKNOWN"
     */
    public Star(){
	this.centerState = unknown;
	neighbourhood = new MyVector();
	
    }


    /**
    * constructor of a star clone of an other.
    * @param s  a Star.
    */   
    public Star(Star s){
	this();
	int i;
	int arity = s.arity();
	this.centerState = new String(s.centerState);
	neighbourhood = new MyVector(arity);
	for(i=0;i<arity;i++){
	    neighbourhood.add((Neighbour)(s.neighbour(i)).clone());
	}
	
    }

    
   /**
    *
    * @param centerState  the label of the center.
    */  
    public Star(String centerState){
	this.centerState = centerState;
	neighbourhood = new MyVector();
    }


    /**
    * create a Star. wich Neighbours doors are numerated from 0 to arity -1.
    * @param centerState the label of the center.
    * @param arity the arity of the star.
    */   
    public Star(String centerState,int arity){
	int i;
	this.centerState = centerState;
	neighbourhood = new MyVector(arity);
	for(i=0;i<arity;i++)
	    {
		
		neighbourhood.add(new  Neighbour(i) );
	    }
    }


    /**
    * create a Star. wich Neighbours doors are numerated from 0 to arity -1.
    * center state is "UNKNOWN"
    * @param arity the arity of the star.
    */   
    public Star(int arity){
	int i;
	this.centerState = unknown;
	neighbourhood = new MyVector(arity);
	for(i=0;i<arity;i++)
	    {
		
		neighbourhood.add(new  Neighbour(i) );
	    }
    }
    public String toString(){
	return "\n<Star>"+centerState+","+this.arity()+" Neighbours:"+ neighbourhood.toString()+"\n<End Star>";
    }
    

    /* accessors */

    public void setCenterState(String state)
    {
	this.centerState = new String(state);
    }


    /**
    *
    * @return the state of the center.
    */   
    public String centerState()
    {
	return new String(this.centerState);
    }


    /**
    *
    * @param i the position
    * @return the Neighbour on the position i.
    */   
    public Neighbour neighbour(int i)
    {
	return (Neighbour)neighbourhood.get(i);
    }


    /**
    * 
    * @param i a position.
    * @return the number of the door of the neighbour on the position i. 
    */   
    public int neighbourDoor(int i){
   	return (((Neighbour) neighbourhood.get(i))).doorNum(); 
    }


    /**
    * add a the Neighbour v to the neighbourhood.
    * the Neighbour is added at the end of the vector.
    * @param v a new Neighbour
    */   
    public void addNeighbour(Neighbour v){
	neighbourhood.add(v);
    }


    /**
    * remove from neighbourhood the neighbour at position i.
    * @param i a position.
    */   
    public void removeNeighbour(int i){
	neighbourhood.remove(i);
    }


    /**
    *remove all elements from neighbourhood.
    */ 
    public void removeAll(){
    	neighbourhood.clear();
    }


    /**
    * sets the Neighbour n at the position i in the neighbourhood.
    * @param position a position in neighbourhood.
    * @param n a Neighbour.
    */  
    public void setState(int position, Neighbour n){
	neighbourhood.setElementAt(n,position);
    }
   

    /**
    *
    * @param s2 star
    * @return true if centers are equals, false otherwoise.
    */    
    public boolean sameCentState(Star s2)
    {
	return this.centerState.equals(s2.centerState());
    }


    /**
    *
    * @return the arity of the star. 
    */   
    public int arity(){
	return (this.neighbourhood.count());
    }


    /**
    *
    * @return the neighbourhood.
    */
    public MyVector neighbourhood(){
	return this.neighbourhood;
    }


    /**
    * sets the door numbers of the star, with the value of door numbers
    * of those at the same position in the star b.
    * @param b a star with the same arity.
    */   
    public void setDoors(Star b){
	int i;
	for(i = 0; i< b.arity(); i++){
	    neighbour(i).setDoorNum(b.neighbour(i).doorNum());
	}
    }


    /**
    * sets states of the star elements (center and neighbours), with the value of 
    * states of those at the same position in the star b.
    * @param b a star with the same arity.
    */   
    public void setStates(Star b){
	int i;
	setCenterState(b.centerState());
	for(i= 0; i<b.arity(); i++){
	    for(int k=0;k< this.arity();k++)
		{
		    if(b.neighbour(i).doorNum() == this.neighbour(k).doorNum())
			setState(k, b.neighbour(i));	
		}
	}
    }


    /* comparators, they use random access */
    /**
     * looks in the star for a Neighbour equals to the Neighbour nei.
     * ATT!!! it also sets the door number of nei with the door number
     * of the element if found!  * the operation looking for is
     * Randomized
    * @param nei
    * @return the index of the element if found. -1 otherwoise.
    */    
    public int contains(Neighbour nei){
	int i = neighbourhood.indexOf((Neighbour)nei);
	if(i > -1){
	    nei.setDoorNum( ((Neighbour) neighbourhood.get(i)).doorNum());
	}
	
	return i;
    }



    /**
    * looks in the star for a Neighbour with the same label of the Neighbour nei.
    * the operation "looking for" is Randomized
    * @param nei
    * @return the index of the element if found. -1 otherwoise.
    */    
    public int containsLabel(Neighbour nei){
	int i = neighbourhood.indexOfLabel((Neighbour)nei);
	if(i > -1){
	}
	return i;
    }
    


    /* we don't begin with the first neighbour, but we choose anyone */
    /**
    * warning: this method sets doors of context by those of
    * corresponding elements in the star.  so always use a copy of the
    * context while using this methode.  the sens of equality is
    * defined in the class Neighbour
    * @param context a context
    * @return true it's to identify the context with a part of teh Star.
    */   
    public  boolean contains(Star context){
	int k = 0;
	int i = -1;
	if(this.sameCentState(context) == false){
	    return false;
	}
	else{
	    Iterator it = context.neighbourhood.randIterator();
	    while( it.hasNext()){
		Neighbour n = (Neighbour) it.next();
		i = this.contains(n);
		if( i < 0){
		    return false;
		}
		else{
		    int door =  ((Neighbour)this.neighbourhood.get(i)).doorNum();      
		    n.setDoorNum(door);
		    this.removeNeighbour(i);
		   
		}
		k++;
	    }
	    
	    return  true;  
	}
    }


    /**
     *
     * @param context a context
     * @return true it's to identify the context with a part of teh Star.
     * the identification concerne only labels.
     */   
    public  boolean containsLabels(Star star){
	int k = 0;
	int i = -1;
	while( k < star.neighbourhood.count() ){
	    i = this.containsLabel(star.neighbour(k));
	    if( i < 0){
		//  System.out.println("<0 ? ="+i+"  (k="+k+")");
		return false;
	    }
	    else{
		this.removeNeighbour(i);
		// System.out.println("i="+i+"k"+k+"door "+door+"=?"+star.neighbour(k));
	    }
	    k++;
	}
	return  true;  
    }



    public Object clone(){
	Star s = new Star(this);
	return s;
    }
    static public void main(String args[])
    {
	//System.out.println("/********* Test of Star ********/");
	MyVector v = new MyVector();
	for(int i=0; i<10; i++){
	    v.add(new Integer(i));
	}
	Iterator it = v.randIterator();
	while(it.hasNext()){
	    //System.out.println("voila "+(Integer)it.next());
	}
	


	Star s = new Star(6);
	//System.out.println("___printing test___");
	//System.out.println(s);
	//System.out.println("___ test of setState ___ ");
	Neighbour n1 = new  Neighbour("A", true);
	Neighbour n2 = new  Neighbour("A", true);
	Neighbour n3 = new  Neighbour("B", true);
	Neighbour n4 = new  Neighbour("B", true);
	s.setState(1,n1);
	s.setState(2,n2);
	s.setState(3,n3);
	//System.out.println(s);
	//System.out.println("? true= "+ n3.equals(n4));
	//System.out.println("? > -1 = "+ s.contains(n4)+" __test of star.contains(Neigh)__");
	//System.out.println("___Testing stars comparison___");	
	Star s2 = new Star(3);
	Neighbour n12 = new  Neighbour("A", true);
	Neighbour n32 = new  Neighbour("B", true);
	s2.setState(0,n1);
      	s2.setState(1,n3);
	//System.out.println("? false= "+ s.contains(s2)+"__portes != -1");
	Neighbour n34 = new  Neighbour("E", true);
	s2.setState(2,n34);
	//System.out.println("? false= "+ s.contains(s2));
	n34 = new Neighbour("N",false);
	s2.setState(2, n34);
	//System.out.println("? true= "+ s.contains(s2));
    }
}
