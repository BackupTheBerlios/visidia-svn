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
	num = new Integer(sn);
    }

    /**
     *
     *
     */
    public int getStateNumber(){
	return sn;
    }    

    /**
     *
     *
     */
    public void setStateNumber(int sn){
	this.sn = sn;
	num = new Integer(sn);
    }

    public Integer number(){
	return num;
    }

    public Object clone(){
	return new State(getStateNumber());
    }

}
