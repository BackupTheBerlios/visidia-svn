package visidia.misc;

import visidia.rule.*;
import java.io.*;

public class Arrow implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2971140425366521313L;

	/**
     * label du premier noeud
     */
    public String left;   //label du premier noeud
   
    /**
     * label du deuxième noeud
     **/ 
    public String right;  

    
    /**
     * vrai si l arête est marquée
     **/
    public boolean isMarked;  
    
    
    public Arrow( String leftState, String rightState){
	this.left =  leftState;
	this.right =  rightState;
	this.isMarked = false;
    }

    public  Arrow( String leftState, String rightState,boolean marquage){
	this.left =  leftState;
	this.right =  rightState;
	this.isMarked = marquage;
    }
    
    // ajout fahsi
    public Arrow(Neighbour n,  String center){ 
	
	this.right = n.state();
	this.left = new String(center);
	this.isMarked = n.mark();
    }

  
    public String right(){
	return this.right;
    }
    
}

