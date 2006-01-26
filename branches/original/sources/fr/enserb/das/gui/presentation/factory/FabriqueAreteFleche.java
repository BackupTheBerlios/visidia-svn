package fr.enserb.das.gui.presentation.factory;

import java.io.Serializable;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.metier.*;

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
