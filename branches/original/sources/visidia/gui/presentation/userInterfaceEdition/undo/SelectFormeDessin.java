package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les selections d'objets dans le graphe. */

public class SelectFormeDessin implements UndoObject {
    
  SelectionDessin selection;
  FormeDessin forme;
  
  /** Construit une nouvelle forme correspondant a la selection de forme de    selection */ 
  public SelectFormeDessin(SelectionDessin selection, FormeDessin forme) {
    this.selection = selection;
    this.forme = forme;
  }
  
  public void undo() {
    forme.enluminer(false);
    selection.supprimerElement(forme);
  }
    
  public void redo() {
    forme.enluminer(true);
    selection.insererElement(forme);
  }

  public FormeDessin content() {
      return forme;
  }

}
  
