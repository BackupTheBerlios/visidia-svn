package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import java.io.*;
import fr.enserb.das.gui.presentation.*;

/** Cette interface est impl�ment�e par les objets repr�sentant des op�rations susceptibles d'�tre annul�es ou restaur�es (cr�ation, suppression, s�lection, d�selection ou d�placements de sommets ou d'ar�tes). */
public interface UndoObject extends Serializable {
  
  /** Cette m�thode annule l'op�ration. */
  public void undo();
  
  /** Cette m�thode r�tablit l'op�ration. */
  public void redo();

  /** Cette m�thode retourne l'objet concern� par l'op�ration. */
  public FormeDessin content();
}
