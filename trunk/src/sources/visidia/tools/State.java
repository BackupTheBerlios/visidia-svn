package visidia.tools;


public class State implements Cloneable{
    private int sn;
    private Integer num;

    /**
     *
     *
     */
    public State(int sn){
	this.sn = sn;
	this.num = new Integer(sn);
    }

    /**
     *
     *
     */
    public int getStateNumber(){
	return this.sn;
    }    

    /**
     *
     *
     */
    public void setStateNumber(int sn){
	this.sn = sn;
	this.num = new Integer(sn);
    }

    public Integer number(){
	return this.num;
    }

    public Object clone(){
	return new State(this.getStateNumber());
    }

}
