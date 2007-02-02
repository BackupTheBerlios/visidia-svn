package visidia.algo;

import visidia.visidiassert.*;
import visidia.misc.*;
import java.io.*;

/**
 * Exemple d'implementation des regles de reecriture,a l'aide de vecteurs.
 */

//import java.util.Vector;

public class SimpleRule  implements Serializable{
    private Arrow leftMember;
    private Arrow rightMember;
   


    public SimpleRule(Arrow gauche,Arrow droite){
	leftMember = gauche;
	rightMember = droite;
    }

    public Arrow getLeft(){
	return leftMember;
    }
    public Arrow getRight(){
	return rightMember;
    }
    public boolean isApplicable(Arrow a){
	if( a.left.equals(leftMember.left)){
	    if( a.right.equals(leftMember.right))
		return a.isMarked == leftMember.isMarked;
		
	    else return false;
	}

	if( a.left.equals(leftMember.right)){
	    if( a.right.equals(leftMember.left))
		return a.isMarked == leftMember.isMarked;
	    else return false;
	} 
	return false ;
    }
    public Arrow apply(Arrow a){
	VisidiaAssertion.verify( isApplicable(a),"regle non applicable",this);

	if( a.left.equals(leftMember.left))
	    return new Arrow( rightMember.left,rightMember.right,rightMember.isMarked);
	else
	    return new Arrow( rightMember.right,rightMember.left,rightMember.isMarked);
	

    }

}


   
