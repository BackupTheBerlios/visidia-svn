package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import fr.enserb.das.gui.presentation.*;



/** Cette classe permet d'annuler et de restaurer les créations d'objets dans un graphe. */

public class AjouteObjet implements UndoObject{
    
  protected FormeDessin maForme;
  
    /** L'forme créé dans le graphe est passé en argument à ce constructeur.*/
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

