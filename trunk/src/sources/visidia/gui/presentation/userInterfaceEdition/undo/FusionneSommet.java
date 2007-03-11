package visidia.gui.presentation.userInterfaceEdition.undo;

import java.util.Enumeration;
import java.util.Vector;

import visidia.gui.metier.Arete;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SommetDessin;

/**
 * Cette classe contient les informations pour annuler les fusions de sommets
 * intervenant lors du deplacement d'un sommet existant : il faut pouvoir
 * recreer le sommet qui est detruit lors la fusion, lui redonner sa position
 * initiale, et reaffecter les extrémites des arêtes.
 */
public class FusionneSommet implements UndoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4954444005187665753L;

	/**
	 * le sommet qui sera detruit par la fusion(le sommet qu'on deplace)
	 */
	protected SommetDessin sommetDetruit;

	/**
	 * le sommet sur lequel est realise la fusion.
	 */
	protected SommetDessin sommetGarde;

	private Vector aretesEntrantes;

	private Vector aretesSortantes;

	/**
	 * l'abscisse initiale du sommet detruit.
	 */
	protected int original_X;

	/**
	 * l'ordonnée initiale du sommet detruit.
	 */
	protected int original_Y;

	public FusionneSommet(SommetDessin sommetDetruit, SommetDessin sommetGarde,
			int original_X, int original_Y) {
		this.sommetDetruit = sommetDetruit;
		this.sommetGarde = sommetGarde;
		this.aretesEntrantes = new Vector();
		this.aretesSortantes = new Vector();
		Enumeration e = sommetDetruit.getSommet().aretesEntrantes();
		while (e.hasMoreElements()) {
			this.aretesEntrantes.addElement(((Arete) e.nextElement())
					.getAreteDessin());
		}
		e = (sommetDetruit.getSommet()).aretesSortantes();
		while (e.hasMoreElements()) {
			this.aretesSortantes.addElement(((Arete) e.nextElement())
					.getAreteDessin());
		}
		this.original_X = original_X;
		this.original_Y = original_Y;
	}

	public void undo() {
		this.sommetDetruit.getVueGraphe().putObject(this.sommetDetruit);
		int i;
		for (i = 0; i < this.aretesSortantes.size(); i++) {
			((AreteDessin) this.aretesSortantes.elementAt(i))
					.changerOrigine(this.sommetDetruit);
		}
		for (i = 0; i < this.aretesEntrantes.size(); i++) {
			((AreteDessin) this.aretesEntrantes.elementAt(i))
					.changerDestination(this.sommetDetruit);
		}
		this.sommetDetruit.placer(this.original_X, this.original_Y);
	}

	public void redo() {
		int i;
		for (i = 0; i < this.aretesSortantes.size(); i++) {
			((AreteDessin) this.aretesSortantes.elementAt(i))
					.changerOrigine(this.sommetGarde);
		}
		for (i = 0; i < this.aretesEntrantes.size(); i++) {
			((AreteDessin) this.aretesEntrantes.elementAt(i))
					.changerDestination(this.sommetGarde);
		}
		this.sommetDetruit.getVueGraphe().delObject(this.sommetDetruit);
	}

	public FormeDessin content() {
		return this.sommetDetruit;
	}
}
