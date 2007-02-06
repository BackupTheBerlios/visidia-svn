package visidia.gui.presentation.factory;

import java.io.Serializable;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class FabriqueAreteFleche implements FabriqueArete,Serializable{

 
    /**
	 * 
	 */
	private static final long serialVersionUID = 472048272251051733L;

	public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a){
	return new AreteFlecheSimple(origine, destination, a);
    }

    
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination){
	return new AreteFlecheSimple(origine, destination);
    }
    
    public String description(){
    	return "Oriented edge";}

}
