package visidia.gui.presentation.factory;

import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public interface FabriqueSommet{

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s);
    
    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label);
    
    public String description();
    
}
