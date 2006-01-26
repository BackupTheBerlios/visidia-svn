package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import fr.enserb.das.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les s�lections d'objets dans le graphe. */

public class SelectFormeDessin implements UndoObject {
    
  SelectionDessin selection;
  FormeDessin forme;
  
  /** Construit une nouvelle forme correspondant � la s�lection de forme de    selection */ 
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
  
