package visidia.gui.presentation.factory;

import java.io.Serializable;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class FabriqueAreteFleche implements FabriqueArete,Serializable{

 
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a){
	return new AreteFlecheSimple(origine, destination, a);
    }

    
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination){
	return new AreteFlecheSimple(origine, destination);
    }
    
    public String description(){
    	return "Oriented edge";}

}
