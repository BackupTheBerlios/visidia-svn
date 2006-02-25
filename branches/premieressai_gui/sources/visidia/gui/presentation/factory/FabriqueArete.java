package visidia.gui.presentation.factory;

import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public interface FabriqueArete{

    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a);

    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination);
    
    public String description();
    
}
