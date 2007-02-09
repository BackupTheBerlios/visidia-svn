package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SelectionDessin;

/** Cette classe permet d'annuler et de restaurer les deselections des
 * formes dans le graphe. */

public class DeselectFormeDessin implements UndoObject {
    
  /**
	 * 
	 */
	private static final long serialVersionUID = 2787468975690725741L;
protected SelectionDessin selection;
  protected FormeDessin forme;

  /** Construit une nouvelle forme correspondant a la deselection de
   * forme de selection */ 
  public DeselectFormeDessin(SelectionDessin selection, FormeDessin forme) {
    this.selection = selection;
    this.forme = forme;
  }
  
  public void undo() {
    this.forme.enluminer(true);
    this.selection.insererElement(this.forme);
  }
    
  public void redo() {
    this.forme.enluminer(false);
    this.selection.supprimerElement(this.forme);
  }

  public FormeDessin content() {
      return this.forme;
  }
}
  
