package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import fr.enserb.das.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les déselections des formes dans le graphe. */

public class DeselectFormeDessin implements UndoObject {
    
  protected SelectionDessin selection;
  protected FormeDessin forme;

  /** Construit une nouvelle forme correspondant à la déselection de forme de    selection */ 
  public DeselectFormeDessin(SelectionDessin selection, FormeDessin forme) {
    this.selection = selection;
    this.forme = forme;
  }
  
  public void undo() {
    forme.enluminer(true);
    selection.insererElement(forme);
  }
    
  public void redo() {
    forme.enluminer(false);
    selection.supprimerElement(forme);
  }

  public FormeDessin content() {
      return forme;
  }
}
  
