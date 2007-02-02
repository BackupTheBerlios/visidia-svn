package visidia.gui.presentation.boite;

import javax.swing.*;
//import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.Editeur;
import visidia.gui.presentation.factory.*; // pour avoir les fabriques

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs
 * permettant d'afficher et de modifier les caracteristiques
 */

public class BoiteChangingVertexShape extends BoiteChangingShape implements ActionListener, ItemListener {
  

    // Variable d'instance
    private String interfaceFactoryName = "FabriqueSommet";
    
    /** Changement de la forme des Sommets */
    protected int chgtForme = -1;
	
    /** La liste des noms de fleches possibles. */
    private Vector vertexNames = new Vector();	
    private Vector vertexFactories = new Vector();			
   
    /** La liste de choix du type de dessin.*/
    protected JComboBox choix_type;
    
    //Construsteurs
    /** Cree une nouvelle boite pour afficher les caracteristiques */
    public BoiteChangingVertexShape(JFrame parent, VueGraphe gr) {
	this(parent,gr, "Vertices shape");
    }
    
    
    /** Crée une nouvelle boite, centrée sur "parent" La boite sera
     * appelée "titre" Suivant la valeur de "est_editable", les
     * caractéristiques des sommets sont modifiables.
     **/
    public BoiteChangingVertexShape(JFrame parent, VueGraphe vg,String titre) {
	super(parent, vg, titre);
	int index = 0;
	
	// fill the vectors for changing the factories
    	try{
    
    	File factoryDirectory = new File(factoryPath);
    	String[] listOfFiles = factoryDirectory.list();
    	Class fabriqueSommet = Class.forName(factoryPointPath+interfaceFactoryName);
    	for (int j=0;j<listOfFiles.length;j++)
     	 if (accept(listOfFiles[j])){ // we keep only .class files
     		if (listOfFiles[j].equals(interfaceFactoryName+".class")) continue; // don't keep interface
        	Class factFile = Class.forName(factoryPointPath+nameWithoutExtension(listOfFiles[j]));
    		if (fabriqueSommet.isAssignableFrom(factFile)) 
    		{
    		   FabriqueSommet ourFactory = (FabriqueSommet)factFile.newInstance();	
    		   vertexNames.add(index,ourFactory.description());
    		   vertexFactories.add(index,ourFactory);
    		   index ++;
    		}
     }
    
    
    }catch(Exception e){System.out.println("Problem : "+e);}
    	
	
	
	// on regarde la fabrique utilisée pour donner la bonne valeur
	// par défaut
	index = 0;
	for (int j=0;j<vertexNames.size();j++)
		if (((String)vertexNames.elementAt(j)).equals(vueGraphe.getFabriqueSommet().description())) 
		    index = j;
	

	choix_type = ligne_choix(caracteristicsPane,
				 "Vertex shape :",
				 vertexNames,
				 est_editable,
				 vertexNames.elementAt(index));
	choix_type.addItemListener(this);
	
    }
    
    
    //Méthodes
     
    /** Cette méthode est appelée si l'utilisateur choisit une
     * nouvelle forme grace a la liste de choix.*/
    public void itemStateChanged(ItemEvent evt) {
	if(evt.getSource() == choix_type) {
	    chgtForme = choix_type.getSelectedIndex();
	    elementModified();
	}
    }

    /** Cette méthode est appelée quand l'utilisateur actionne un des
     * boutons de la boite.*/
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
    }

   /** Cette méthode est appelée quand l'utilisateur appuie sur le
    * bouton Ok */ 
    public void buttonOk() {
	if (chgtForme != -1){
	  try {
	      vueGraphe.changerFormeSommet((FabriqueSommet)vertexFactories.elementAt(chgtForme),((Editeur)parent).getUndoInfo());
	      ((Editeur)parent).setUndo();
	  } catch(Exception expt) {
	      System.out.println("Problem : " + expt);
	  }
	}
    }
}
