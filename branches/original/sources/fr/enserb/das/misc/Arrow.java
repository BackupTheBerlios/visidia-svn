package fr.enserb.das.misc;


import fr.enserb.das.misc.*;
import java.io.*;
public class Arrow implements Serializable {
    public String left;   //label du premier noeud
    public String right;  //label du deuxieme noeud
    public boolean isMarked;  //vrai si l arete est marquee
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


}

