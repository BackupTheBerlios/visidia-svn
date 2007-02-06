package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les séléctions
 * d'objets dans le graphe. */

public class SelectFormeDessin implements UndoObject {
    
  /**
	 * 
	 */
	private static final long serialVersionUID = 803403697548600203L;
SelectionDessin selection;
  FormeDessin forme;
  
  /** Construit une nouvelle forme correspondant à la séléction de
   * forme de selection */ 
  public SelectFormeDessin(SelectionDessin selection, FormeDessin forme) {
    this.selection = selection;
    this.forme = forme;
  }
  
  public void undo() {
    this.forme.enluminer(false);
    this.selection.supprimerElement(this.forme);
  }
    
  public void redo() {
    this.forme.enluminer(true);
    this.selection.insererElement(this.forme);
  }

  public FormeDessin content() {
      return this.forme;
  }

}
  
