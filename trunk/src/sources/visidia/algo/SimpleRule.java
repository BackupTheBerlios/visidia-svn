package visidia.algo;

import visidia.visidiassert.*;
import visidia.misc.*;
import java.io.*;

/**
 * Exemple d'implementation des regles de reecriture,a l'aide de vecteurs.
 */


public class SimpleRule  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3872496695068104648L;
	private Arrow leftMember;
    private Arrow rightMember;
   


    public SimpleRule(Arrow gauche,Arrow droite){
	this.leftMember = gauche;
	this.rightMember = droite;
    }

    public Arrow getLeft(){
	return this.leftMember;
    }
    public Arrow getRight(){
	return this.rightMember;
    }
    public boolean isApplicable(Arrow a){
	if( a.left.equals(this.leftMember.left)){
	    if( a.right.equals(this.leftMember.right))
		return a.isMarked == this.leftMember.isMarked;
		
	    else return false;
	}

	if( a.left.equals(this.leftMember.right)){
	    if( a.right.equals(this.leftMember.left))
		return a.isMarked == this.leftMember.isMarked;
	    else return false;
	} 
	return false ;
    }
    public Arrow apply(Arrow a){
	VisidiaAssertion.verify( this.isApplicable(a),"regle non applicable",this);

	if( a.left.equals(this.leftMember.left))
	    return new Arrow( this.rightMember.left,this.rightMember.right,this.rightMember.isMarked);
	else
	    return new Arrow( this.rightMember.right,this.rightMember.left,this.rightMember.isMarked);
	

    }

}


   
