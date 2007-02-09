package visidia.rule;

import java.io.Serializable;

import visidia.misc.Arrow;


/**  a Neighbour contains all the information concerning a node.
 *
 */
public class Neighbour implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8368204779367407395L;
	protected String state;
    protected boolean mark;
    protected int door;
    static String unknown ="UNKNOWN";

    /**   the default constructor
     * default values are : "UNKNOWN", false , -1
     */
    public Neighbour()
    {
	this.state =unknown;
	this.mark= false;
	this.door = -1;
    }
     
    /**
     * constructor.
     * @param state the new Label
     * @param edgMark the marque of the edg
     * @param door the number of the door
     * @return 
     */   

    public Neighbour(String state, boolean edgMark, int door)
    {
	this.state = state;
	this.mark= edgMark;
	this.door = door;
    }
    

    public Neighbour(String state, boolean edgMark)
    {
	this(state, edgMark, -1);
    }
 
    public Neighbour(boolean edgMark, int door)
    {
	this(unknown, edgMark, door);
    }

    

    /**
     *
     * @param state the label of the node
     * @return 
     */   
    public Neighbour(String state){
	this(state, false, -1);
    }
    public Neighbour(int door){
	this(unknown, false, door);
    }
    
    
    /**
     * a constructor from an object Arrow. the state corresponds to the right value and door is not affected. 
     * this method is not used.
     * @param r the Arrow
     */    
    public Neighbour(Arrow r){
	this.state = r.right();
	this.mark= r.isMarked;
	this.door = -1;
    }


    
    /** 
     * a constructor from an object Arrow. the state corresponds to the right value.
     * this method is not used.
     *
     * @param r an Arrow
     * @param door the new number of the door.
     */   
    public Neighbour(Arrow r, int door){
	this.state = r.right();
	this.mark= r.isMarked;
	this.door = door;
    }
    
    public String toString()
    {
	return " Neigh("+this.state+"_"+this.door+"_"+this.mark+")";
    }
    


    /* accessors */
    /**
     * return the door number.
     * @return accessor to door.
     */   
    public int doorNum(){
	return this.door;
    }
    


    /**
     * set the value of door.
     * @param n the new door number.
     */   
    public void setDoorNum(int n){
	this.door=n;
    }
    





    /**
     * sets proprieties with value of those of the Neighbour given on parameters. 
     * only the door number is not seted.
     * @param n a Neighbour.
     */ 
    public void setState(Neighbour n){//not doors
	this.mark = n.mark();
	this.state = n.state();
    }
    



    /**
     *return the edg mark.
     * @return the edg mark. 
     */   
    public boolean mark()
    {
	return this.mark;
    }
    


    /**
     * return the state ( a )
     * @return the stae (label). 
     */   
    public String state()
    {
	return this.state;
    }
    

    public Object clone(){
	return new Neighbour(this.state, this.mark,this.door);   
    }
    



    /* comparators: doors are not compared, but door shoud not be affected */
    /**
     * two neighbours are equal if they have same labels, marks, and doors.
     *  doors are not compared if not  affected ( -1 ).
     * @param n
     * @return true if Neighbours are equals, -1 otherweise.
     */   
    public boolean equals(Neighbour n){
	if((this.mark == n.mark) && this.state.equals(n.state)){
	    if(n.door == -1)
		return true;
	    return (this.door == n.door);
	}
	return false;
    }
    


    /**
     * comape only the labels.
     *  
     * @param n
     * @return true if labels are equals, -1 otherweise.
     */   
    public boolean sameState(Neighbour n){
	return (this.state.equals(n.state));
    }
    

    /*test   */
    //static  public void main(String args[])
    //{
	//Neighbour n1 = new Neighbour("A", true);
	//System.out.println(n1);
	//Neighbour n2 = new Neighbour("B");
 	//System.out.println(n2);
	//System.out.println(n1.equals(n2));
    //}
    
}
 
