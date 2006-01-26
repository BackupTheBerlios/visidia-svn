package fr.enserb.das.gui.presentation.boite;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.Editeur;
import fr.enserb.das.gui.presentation.factory.*; // pour avoir les fabriques

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs permettant  
 * d'afficher et de modifier les caractéristiques          */
public class BoiteChangingEdgeShape extends BoiteChangingShape implements ActionListener, ItemListener {
  

    // Variable d'instance
    private String interfaceFactoryName = "FabriqueArete";
    
    /** Changement de la forme de l'arete */
    protected int chgtForme = -1;
	
    /** La liste des noms de flèches possibles. */
    private Vector edgesNames = new Vector();	
    private Vector edgesFactories = new Vector();			
   
    /** La liste de choix du type de dessin.*/
    protected JComboBox choix_type;
    
    //Construsteurs
    /** Crée une nouvelle boite pour afficher les caractéristiques de "une_arete"*/
    public BoiteChangingEdgeShape(JFrame parent, VueGraphe gr) {
	this(parent,gr, "Edges shape");
    }
    
    
    /** Crée une nouvelle boite, centrée sur "parent" pour afficher les caractéristiques de "une_arete".
     * La boite sera appelée "titre"
     * Suivant la valeur de "est_editable", les caractéristiques de l'arête sont modifiables.
     **/
    public BoiteChangingEdgeShape(JFrame parent, VueGraphe vg,String titre) {
	super(parent, vg, titre);
	int index = 0;
	
	// fill the vectors for changing the factories
    	try{
    
    	File factoryDirectory = new File(factoryPath);
    	String[] listOfFiles = factoryDirectory.list();
    	Class fabriqueArete = Class.forName(factoryPointPath+interfaceFactoryName);
    	for (int j=0;j<listOfFiles.length;j++)
     	 if (accept(listOfFiles[j])){ // we keep only .class files
     		if (listOfFiles[j].equals(interfaceFactoryName+".class")) continue; // don't keep interface
        	Class factFile = Class.forName(factoryPointPath+nameWithoutExtension(listOfFiles[j]));
    		if (fabriqueArete.isAssignableFrom(factFile)) 
    		{
    		   FabriqueArete ourFactory = (FabriqueArete)factFile.newInstance();	
    		   edgesNames.add(index,ourFactory.description());
    		   edgesFactories.add(index,ourFactory);
    		   index ++;
    		}
     }
    
    
    }catch(Exception e){System.out.println("Problem : "+e);}
    	
	
	
	// on regarde la fabrique utilisee pour donner la bonne valeur par defaut
	index = 0;
	for (int j=0;j<edgesNames.size();j++)
		if (((String)edgesNames.elementAt(j)).equals(vueGraphe.getFabriqueArete().description())) 
		    index = j;
	

	choix_type = ligne_choix(caracteristicsPane,
				 "Edge shape :",
				 edgesNames,
				 est_editable,
				 edgesNames.elementAt(index));
	choix_type.addItemListener(this);
	
    }
    
    
    //Methodes
     
    /** Cette méthode est appelée si l'utilisateur choisit une nouvelle forme grâce à la liste de choix.*/
    public void itemStateChanged(ItemEvent evt) {
	if(evt.getSource() == choix_type) {
	    chgtForme = choix_type.getSelectedIndex();
	    elementModified();
	}
    }

    /** Cette méthode est appelée quand l'utilisateur actionne un des boutons de la boite.*/
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
    }

   /** Cette méthode est appelée quand l'utilisateur appuie sur le bouton Ok */ 
    public void buttonOk() {
	if (chgtForme != -1){
	    try {
		vueGraphe.changerFormeArete((FabriqueArete)edgesFactories.elementAt(chgtForme),((Editeur)parent).getUndoInfo());
		((Editeur)parent).setUndo();
	    } catch(Exception expt) {
		System.out.println("Problem : " + expt);
	    }
	}
    }
    
}
