/**
 * Algorithme distribué d'election dans un graphe anonyme.
 * Marche bien pour les arbres,et les anneaux, et en général pour
 * les réseaux dont le diamètre (plus grand chemin) est inferieur au plus
 * petit cycle.Pour avoir un algorithme qui marche pour tout type de 
 * réseau, on doit pouvoir connaitre le degrès du réseau.
 **/

package visidia.algoRMI;

import java.util.Random;
import java.util.Vector;

import visidia.misc.VectorMessage;
import visidia.simulation.AlgorithmDist;
import visidia.simulation.Door;

public class ElectionRmi extends AlgorithmDist {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143775012864030440L;

	/**
	 * Retourne <code>true</code> si le noeud est elu ou recoit un message de
	 * fin .
	 */
	private boolean isFinished(int ig) {
		String mystate = this.getState();
		if (mystate.equals("G"))
			return true;

		if (mystate.equals("F"))
			return true;

		return false;
	}

	/**
	 * Algorithme d'election.L'algorithme implémente est celui de Lelanns, avec
	 * quelques variantes.Tous les noeuds sont "candidats".Chacun tire un nombre
	 * au hasard,et fait circuler ce nombre dans le reseau, ainsi que son
	 * identite (pour identifier le message). Le noeud qui a tire le plus grand
	 * nombre sera elu.Si deux noeuds ont tire le meme nombre,on selectionne sur
	 * l'identite du noeud.
	 */
	public void init() {
		// Amelioration rajouter un tableau pour tous les noeuds si N est connu
		int arity = this.getArity();

		// je suis candidat
		this.setState("C");

		// choix d'un nombre au hasard
		int mynumber = (new Random()).nextInt() * this.getId().intValue();
		// j envoie mon tirage a tout mes voisins
		for (int i = 0; i < arity; i++) {
			Vector vec = new Vector(2);
			vec.add(this.getId());
			vec.add(new Integer(mynumber));
			this.sendTo(i, new VectorMessage((Vector) vec.clone()));
		}

		// debut de la procedure :
		while (!this.isFinished(mynumber)) {
			Door porte = new Door();
			VectorMessage vmsg = (VectorMessage) this.receive(porte);
			Vector data = vmsg.data();
			int sender = porte.getNum();

			// message de fin ?
			if (data.elementAt(0) instanceof Boolean) {
				this.setState("F");
				for (int i = 0; i < arity; i++) {
					if (i != sender)
						this
								.sendTo(i, new VectorMessage((Vector) data
										.clone()));
				}
				break;
			}

			int senderId = ((Integer) data.elementAt(0)).intValue();
			int hisnumber = ((Integer) data.elementAt(1)).intValue();

			if (this.getState().equals("C")) {
				// mon message ? --> j'ai gagne
				if (senderId == this.getId().intValue()) {
					this.setState("G");
					// printStatistics();
					Vector v = new Vector(1);
					v.add(new Boolean(true));
					for (int i = 0; i < arity; i++) {
						this.sendTo(i, new VectorMessage((Vector) v.clone()));
					}
				}
				// le nombre tire est plus grand que le mien -->j'ai perdu
				else if (hisnumber > mynumber) {
					this.setState("P");
					for (int i = 0; i < arity; i++) {
						if (i != sender) {
							Vector vec = new Vector(2);
							vec.add(new Integer(senderId));
							vec.add(new Integer(hisnumber));
							this.sendTo(i, new VectorMessage((Vector) vec
									.clone()));
						}
					}
				}

				// si le nombre est le meme -->selection sur le nodeId
				else if (hisnumber == mynumber) {
					if (this.getId().intValue() > senderId) {
						this.setState("G");
						// printStatistics();
						Vector v = new Vector(1);
						v.add(new Boolean(true));
						for (int i = 0; i < arity; i++) {
							this.sendTo(i,
									new VectorMessage((Vector) v.clone()));
						}
					} else {
						this.setState("P");
						for (int i = 0; i < arity; i++) {
							if (i != sender) {
								Vector vec = new Vector(2);
								vec.add(new Integer(senderId));
								vec.add(new Integer(hisnumber));
								this.sendTo(i, new VectorMessage((Vector) vec
										.clone()));
							}
						}
					}
				}
			} else { // mon etat est "P"
				for (int i = 0; i < arity; i++) {
					if (i != sender) {
						Vector vec = new Vector(2);
						vec.add(new Integer(senderId));
						vec.add(new Integer(hisnumber));
						this.sendTo(i, new VectorMessage((Vector) vec.clone()));
					}
				}
			}
		}
	}

	public String getState() {
		return (String) this.getProperty("label");
	}

	public void setState(String newState) {
		this.putProperty("label", newState);
	}

	public Object clone() {
		return new ElectionRmi();
	}

}
