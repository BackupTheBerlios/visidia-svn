package visidia.gui.presentation.boite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.factory.FabriqueArete;
import visidia.gui.presentation.userInterfaceEdition.Editeur;

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs
 * permettant d'afficher et de modifier les caracteristiques  
 *
 */
public class BoiteChangingEdgeShape extends BoiteChangingShape implements ActionListener, ItemListener {
  

    // Variable d'instance
    private String interfaceFactoryName = "FabriqueArete";
    
    /** Changement de la forme de l'arête */
    protected int chgtForme = -1;
	
    /** La liste des noms de flèches possibles. */
    private Vector edgesNames = new Vector();	
    private Vector edgesFactories = new Vector();			
   
    /** La liste de choix du type de dessin.*/
    protected JComboBox choix_type;
    
    //Construsteurs
    /** Cree une nouvelle boite pour afficher les caractéristiques de
     * "une_arete"*/
    public BoiteChangingEdgeShape(JFrame parent, VueGraphe gr) {
	this(parent,gr, "Edges shape");
    }
    
    
    /** Crée une nouvelle boite, centrée sur "parent" pour afficher
     * les caractéristiques de "une_arete".  La boite sera appelée
     * "titre" Suivant la valeur de "est_editable", les
     * caracteristiques de l'arête sont modifiables.
     **/
    public BoiteChangingEdgeShape(JFrame parent, VueGraphe vg,String titre) {
	super(parent, vg, titre);
	int index = 0;
	
	// fill the vectors for changing the factories
    	try{
    
    	File factoryDirectory = new File(this.factoryPath);
    	String[] listOfFiles = factoryDirectory.list();
    	Class fabriqueArete = Class.forName(this.factoryPointPath+this.interfaceFactoryName);
    	for (String element : listOfFiles)
			if (this.accept(element)){ // we keep only .class files
				if (element.equals(this.interfaceFactoryName+".class")) continue; // don't keep interface
				Class factFile = Class.forName(this.factoryPointPath+this.nameWithoutExtension(element));
				if (fabriqueArete.isAssignableFrom(factFile)) 
				{
				   FabriqueArete ourFactory = (FabriqueArete)factFile.newInstance();	
				   this.edgesNames.add(index,ourFactory.description());
				   this.edgesFactories.add(index,ourFactory);
				   index ++;
				}
    }
    
    
    }catch(Exception e){System.out.println("Problem : "+e);}
    	
	
	
	// on regarde la fabrique utilisée pour donner la bonne valeur
	// par défaut
	index = 0;
	for (int j=0;j<this.edgesNames.size();j++)
		if (((String)this.edgesNames.elementAt(j)).equals(VueGraphe.getFabriqueArete().description())) 
		    index = j;
	

	this.choix_type = this.ligne_choix(this.caracteristicsPane,
				 "Edge shape :",
				 this.edgesNames,
				 this.est_editable,
				 this.edgesNames.elementAt(index));
	this.choix_type.addItemListener(this);
	
    }
    
    
    //Methodes
     
    /** Cette méthode est appelée si l'utilisateur choisit une
     * nouvelle forme grace à la liste de choix.*/
    public void itemStateChanged(ItemEvent evt) {
	if(evt.getSource() == this.choix_type) {
	    this.chgtForme = this.choix_type.getSelectedIndex();
	    this.elementModified();
	}
    }

    /** Cette méthode est appelée quand l'utilisateur actionne un des
     * boutons de la boite.*/
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
    }

   /** Cette methode est appelée quand l'utilisateur appuie sur le
    * bouton Ok */ 
    public void buttonOk() {
	if (this.chgtForme != -1){
	    try {
		this.vueGraphe.changerFormeArete((FabriqueArete)this.edgesFactories.elementAt(this.chgtForme),((Editeur)this.parent).getUndoInfo());
		((Editeur)this.parent).setUndo();
	    } catch(Exception expt) {
		System.out.println("Problem : " + expt);
	    }
	}
    }
    
}
