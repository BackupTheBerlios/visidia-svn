
package visidia.algo2;

public class Know {
	
	private int maxNumber=0; /* le nombre maximal de l'ensemble*/
	private int[] setKnowledge     ; /* l'ensemble des numeros envoyes */
	private int myName=0; /* le nom du noeud */
	
	
	public void Initial(int graphS) {
	    /*	    System.out.println("graph size=" +graphS );*/
	    setKnowledge=new int[graphS+1];
	    setKnowledge[0]=0;
	    for (int i=1;i<=graphS;i++) {
		setKnowledge[i]=-1;
		/*		System.out.println(i+"="+setKnowledge[i] );*/
	    }
	}

	public void ChangeKnowledge(int numNoeud,int newNumber) {  
	    if (setKnowledge[numNoeud] < newNumber) {
	    setKnowledge[numNoeud]=newNumber;
	    }
	    if (numNoeud > maxNumber) {
		maxNumber=numNoeud;
	    }
	    if (newNumber > maxNumber) {
		maxNumber=newNumber;
	    }
	}

	public int NeighbourNode(int neighbourName) {
	    return setKnowledge[neighbourName];
	}
	public int Neighbour() { /* Fonction qui nous donne les voisins */
	    return setKnowledge[0];
	}
	
	public int Max() { /* renvois le numero maximal */
	    return maxNumber;
	}

	public void ChangeName(int newName) {
	    myName=newName;
	    if (myName > maxNumber)
		maxNumber=myName;
	}
	
	public int MyName() {
	    return myName;
	}
	
    }
    




