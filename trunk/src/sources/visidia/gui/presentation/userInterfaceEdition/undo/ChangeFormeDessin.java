package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;

/** Cette classe permet d'annuler et de restaurer les deselections des
 * formes dans le graphe. */

public class ChangeFormeDessin implements UndoObject {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6627812271765512580L;
	protected FormeDessin ancienne_forme;
    protected FormeDessin nouvelle_forme;

  /** Construit une nouvelle forme correspondant à la deselection de
   * forme de selection */ 
  public ChangeFormeDessin(FormeDessin ancienne, FormeDessin nouvelle) {
    this.ancienne_forme = ancienne;
    this.nouvelle_forme = nouvelle;
  }
  
  public void undo() {
      this.nouvelle_forme.getVueGraphe().insererListeAffichage(this.ancienne_forme);
      this.ancienne_forme.setObjetGraphe(this.nouvelle_forme.getObjetGraphe());
      this.miseAJourModel(this.ancienne_forme);
      this.nouvelle_forme.getVueGraphe().supprimerListeAffichage(this.nouvelle_forme);
  }
    
  public void redo() {
      this.ancienne_forme.getVueGraphe().insererListeAffichage(this.nouvelle_forme);
      this.nouvelle_forme.setObjetGraphe(this.ancienne_forme.getObjetGraphe());
      this.miseAJourModel(this.nouvelle_forme);
      this.ancienne_forme.getVueGraphe().supprimerListeAffichage(this.ancienne_forme);
  }

  private void miseAJourModel(FormeDessin f){
      if (f.type().equals("vertex"))
	  ((SommetDessin)f).getSommet().setSommetDessin((SommetDessin)f);
      else if (f.type().equals("edge"))
	  ((AreteDessin)f).getArete().setAreteDessin((AreteDessin)f);
  }
      

  public FormeDessin content() {
      return this.ancienne_forme;
  }
}
  
