package visidia.algo2;

import java.util.Vector;

public class Knowledge {
	
	private int maxNumber=0; /* le nombre maximal de l'ensemble*/
	private Vector[] setKnowledge     ; /* l'ensemble des numeros envoyes */
	private int myName=0; /* le nom du noeud */
	
	
	public void initial(int graphS) {
	    this.setKnowledge=new Vector[graphS+1];
	    this.setKnowledge[0]=new Vector();
	    this.setKnowledge[0].add(new Integer(0));
	    for (int i=1;i<=graphS;i++) {
		this.setKnowledge[i]=null;
	    }
	}
	
	public boolean maxSet(Vector v1,Vector v2) {
	    int i,j;
	    i=0;
	    j=0;
	    	     
	    if (v2==null)
		return true;
	    if (v1==null)
		return false;
	    
	    while (true) {
		if (v1.size()>i) {
		    if (v2.size()<=j) 
			return true;
		    else
			if (((Integer) v1.elementAt(i)).intValue()==((Integer) v2.elementAt(j)).intValue()) {
			    i++;
			    j++;
			}
			else
			    if (((Integer) v1.elementAt(i)).intValue()>((Integer) v2.elementAt(j)).intValue()) {
				return true;
			    }
			    else 
				return false;
			    		
		}
		else 	    
		    return false;
		
	    }
	}
	
	public void changeKnowledge(Vector newVector) {  
	    Vector intVector=newVector;
	    int numNoeud=((Integer) newVector.elementAt(0)).intValue();

	    intVector.removeElementAt(0);
	    if (this.maxSet(intVector,this.setKnowledge[numNoeud])) {
		this.setKnowledge[numNoeud]=intVector;
	    }
	    if (numNoeud > this.maxNumber) {
		this.maxNumber=numNoeud;
	    }
	}

	public void changeNeighbours(Vector newName) {
	    int longKnow=this.setKnowledge[0].size();

	    if ( ((Integer)newName.elementAt(0)).intValue() != ((Integer)newName.elementAt(1)).intValue() ) {
		//System.out.println("Vecteur = " +setKnowledge[0]);
		//System.out.println("longueur avant= " +longKnow);
		boolean b=this.setKnowledge[0].remove(newName.elementAt(1));
		//System.out.println("longueur apres= " +setKnowledge[0].size());
		longKnow=this.setKnowledge[0].size();
		
		if (this.setKnowledge[0].isEmpty())
		    this.setKnowledge[0].add(newName.elementAt(0));
		else {
		    if (longKnow>1) {
			if (((Integer)this.setKnowledge[0].elementAt(0)).intValue()<((Integer)newName.elementAt(0)).intValue()) {
			    this.setKnowledge[0].insertElementAt(newName.elementAt(0),0);
			}
			else {
			    // System.out.println("Longueur= "+longKnow+" ; vecteur = "+setKnowledge[0]);
			    //System.out.println(" vecteur 2 = "+newName);
			    if (((Integer)this.setKnowledge[0].elementAt(longKnow-1)).intValue() > ((Integer)newName.elementAt(0)).intValue()) 
				this.setKnowledge[0].addElement(newName.elementAt(0));
			    else {
				for (int i=0;i<longKnow-1;i++)
				    if ((((Integer)this.setKnowledge[0].elementAt(i)).intValue()>((Integer)newName.elementAt(0)).intValue()) && (((Integer)this.setKnowledge[0].elementAt(i+1)).intValue()<=((Integer)newName.elementAt(0)).intValue())) {
					this.setKnowledge[0].insertElementAt(newName.elementAt(0),i+1);
					break;
				    }
			    }
			}
		    }
		    else {
			if (((Integer)this.setKnowledge[0].elementAt(0)).intValue()>((Integer)newName.elementAt(0)).intValue())
			    this.setKnowledge[0].addElement(newName.elementAt(0));
			else
			    this.setKnowledge[0].insertElementAt(newName.elementAt(0),0);
		    }
		}
	    } 
	}

	public Vector neighbourNode(int neighbourName) {
	    return this.setKnowledge[neighbourName];
	}
	public Vector neighbour() { /* Fonction qui nous donne les voisins */
	    return this.setKnowledge[0];
	}
	
	public int max() { /* renvois le numero maximal */
	    return this.maxNumber;
	}

	public void changeName(int newName) {
	    this.myName=newName;
	    if (this.myName > this.maxNumber)
		this.maxNumber=this.myName;
	}
	
	public int myName() {
	    return this.myName;
	}
	
	public boolean endKnowledge(int graphS) {
	    boolean var=true;
	    int k;
	    
	    for (int i=1;i<=graphS;i++) {
		if (this.setKnowledge[i]==null) {
		    var=false;
		    break;
		}
		else
		    if (this.setKnowledge[i].contains(new Integer(0))) {
			var=false;
			break;
		    }
	    }
	    
	    if (var) {
		for (int i=1;i<=graphS;i++)
		    for (int j=0;j<this.setKnowledge[i].size();j++) {
			k=((Integer)this.setKnowledge[i].elementAt(j)).intValue();
			if (!this.setKnowledge[k].contains(new Integer(i))) {
			    var=false;
			    break;
			}
		    }
		
	    }
	    
	    return var;
	}
	
	/*	public void afficherKnowledge(int graphS) {
	    String mess="";
	    
	    for (int i=1;i<=graphS;i++) 
		mess=mess+"["+i+"]="+neighbourNode(i)+"\n";
	    
	   javax.swing.JOptionPane.showMessageDialog(null, mess, "Knowledge",INFORMATION_MESSAGE );
	   }*/
    }
    
