package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;
import visidia.gui.metier.*;

/** Cette classe permet d'annuler et de restaurer les suppressions d'objets dans un graphe. */
public class SupprimeObjet implements UndoObject {
    
  protected FormeDessin forme;
  
  /** L'objet supprime dans le graphe est passe en argument a ce constructeur.*/
  public SupprimeObjet(FormeDessin forme) {
    this.forme = forme;
  }
  
  public void undo() {
    forme.getVueGraphe().putObject(forme);
  }
  
  public void redo() {
    forme.getVueGraphe().delObject(forme);
  }
  
  public FormeDessin content() {
      return forme;
  }
}
