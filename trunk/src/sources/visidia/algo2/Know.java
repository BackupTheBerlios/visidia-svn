package visidia.algo2;

public class Know {

	private int maxNumber = 0; /* le nombre maximal de l'ensemble */

	private int[] setKnowledge; /* l'ensemble des numeros envoyes */

	private int myName = 0; /* le nom du noeud */

	public void Initial(int graphS) {
		/* System.out.println("graph size=" +graphS ); */
		this.setKnowledge = new int[graphS + 1];
		this.setKnowledge[0] = 0;
		for (int i = 1; i <= graphS; i++) {
			this.setKnowledge[i] = -1;
			/* System.out.println(i+"="+setKnowledge[i] ); */
		}
	}

	public void ChangeKnowledge(int numNoeud, int newNumber) {
		if (this.setKnowledge[numNoeud] < newNumber) {
			this.setKnowledge[numNoeud] = newNumber;
		}
		if (numNoeud > this.maxNumber) {
			this.maxNumber = numNoeud;
		}
		if (newNumber > this.maxNumber) {
			this.maxNumber = newNumber;
		}
	}

	public int NeighbourNode(int neighbourName) {
		return this.setKnowledge[neighbourName];
	}

	public int Neighbour() { /* Fonction qui nous donne les voisins */
		return this.setKnowledge[0];
	}

	public int Max() { /* renvois le numero maximal */
		return this.maxNumber;
	}

	public void ChangeName(int newName) {
		this.myName = newName;
		if (this.myName > this.maxNumber) {
			this.maxNumber = this.myName;
		}
	}

	public int MyName() {
		return this.myName;
	}

}
