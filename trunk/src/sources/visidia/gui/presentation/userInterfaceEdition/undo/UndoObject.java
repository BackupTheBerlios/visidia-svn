package visidia.gui.presentation.userInterfaceEdition.undo;

import java.io.Serializable;

import visidia.gui.presentation.FormeDessin;

/**
 * Cette interface sera implémentée par les objets représentant des opérations
 * susceptibles d'être annulées ou restaurées (creation, suppression, selection,
 * deselection ou déplacements de sommets ou d'arêtes).
 */
public interface UndoObject extends Serializable {

	/** Cette méthode annule l'opération. */
	public void undo();

	/** Cette méthode rétablit l'opération. */
	public void redo();

	/** Cette méthode retourne l'objet concerné par l'opération. */
	public FormeDessin content();
}
