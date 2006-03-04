package visidia.misc;

import visidia.rule.*;
import visidia.misc.*;
import java.io.*;

public class Arrow implements Serializable {
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
	left =  leftState;
	right =  rightState;
	isMarked = false;
    }

    public  Arrow( String leftState, String rightState,boolean marquage){
	left =  leftState;
	right =  rightState;
	isMarked = marquage;
    }
    
    // ajout fahsi
    public Arrow(Neighbour n,  String center){ 
	
	right = n.state();
	left = new String(center);
	isMarked = n.mark();
    }

  
    public String right(){
	return this.right;
    }
    
}

