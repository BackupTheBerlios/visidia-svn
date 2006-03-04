package visidia.gui.metier.inputOutput;

import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.boite.*;

public class NewSimpleRule{
    
 public static void newRule(FenetreDeSimulation fenetre) {
	
	BoiteSimpleRule boiteSimpleRule = new BoiteSimpleRule(fenetre,"Regle Numero" + fenetre.numberOfRules());
        boiteSimpleRule.show(fenetre);
	
		

 	fenetre.pack();
 	fenetre.setVisible(true);
  }
    
}
