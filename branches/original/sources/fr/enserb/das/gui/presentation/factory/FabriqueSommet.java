package fr.enserb.das.gui.presentation.factory;

import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.metier.*;

public interface FabriqueSommet{

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s);
    
    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label);
    
    public String description();
    
}
