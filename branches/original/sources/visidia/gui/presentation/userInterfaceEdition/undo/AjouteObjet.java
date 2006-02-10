package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;



/** Cette classe permet d'annuler et de restaurer les creations d'objets dans un graphe. */

public class AjouteObjet implements UndoObject{
    
  protected FormeDessin maForme;
  
    /** L'forme cree dans le graphe est passe en argument a ce constructeur.*/
  public AjouteObjet(FormeDessin forme) {
    maForme = forme;
  }
  
  public void undo() {
    maForme.getVueGraphe().delObject(maForme);
  }
    
  public void redo() {
    maForme.getVueGraphe().putObject(maForme);
  }
  
  public FormeDessin content() {
      return maForme;
  }
}

