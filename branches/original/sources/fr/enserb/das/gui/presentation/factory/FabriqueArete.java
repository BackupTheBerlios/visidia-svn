package fr.enserb.das.gui.presentation.factory;

import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.metier.*;

public interface FabriqueArete{

    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a);

    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination);
    
    public String description();
    
}
