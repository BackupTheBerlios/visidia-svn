package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les suppressions
 * d'objets dans un graphe. */
public class SupprimeObjet implements UndoObject {
    
  /**
	 * 
	 */
	private static final long serialVersionUID = -9174487500722057854L;
protected FormeDessin forme;
  
  /** L'objet supprimé dans le graphe est passé en argument à ce
   * constructeur.*/
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
