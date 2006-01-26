package fr.enserb.das.gui.metier.inputOutput;

import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.gui.presentation.boite.*;

public class NewSimpleRule{
    
 public static void newRule(FenetreDeSimulation fenetre) {
	
	BoiteSimpleRule boiteSimpleRule = new BoiteSimpleRule(fenetre,"Regle Numero" + fenetre.numberOfRules());
        boiteSimpleRule.show(fenetre);
	
		

 	fenetre.pack();
 	fenetre.setVisible(true);
  }
    
}
