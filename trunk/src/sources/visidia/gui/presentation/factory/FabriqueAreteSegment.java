package visidia.gui.presentation.factory;

import java.io.Serializable;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class FabriqueAreteSegment implements FabriqueArete,Serializable{

    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a){
	return new AreteSegment(origine, destination,a);
    }
 
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination){
	return new AreteSegment(origine, destination);
    }
    
    public String description(){
    	return "Non oriented edge";}
}

