package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import visidia.gui.metier.Sommet;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import visidia.tools.LocalNodeTable;



/**
 * Cette classe cree une boite pour specifier l'emplacement des serveurs 
 * relatifs a chaque noeud du graphe.   
 */
public class BoiteDistribue implements ActionListener {
    
    protected String DEFAULT_URL_FOR_SIMULATOR = "Simulator";
    protected VueGraphe vueGraphe;
    protected Enumeration sommets;
    protected int nbrSommets; 
    /** La fenetre parent : la boite sera centree sur cette fenetre.*/
    protected FenetreDeSimulationDist parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    protected JScrollPane scroller;
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;
    protected JPanel labelPanel, buttonPane, mainPane,toolBarPanel;
    protected JLabel nomSimuLabel,hostLabel,portLabel,localNodeLabel;
    protected JTextField nomSimuField, nomSimuPort, nomSimuUrl;
    
    protected String[] nomDesSommets;
    protected static final String DEFAULT_PORT = "1099";
        
    //Constructeurs
    public BoiteDistribue(FenetreDeSimulationDist parent, VueGraphe vueGraphe, String titre) {
	
	this.dialog = new JDialog(parent, titre);
	this.parent = parent;
	this.vueGraphe = vueGraphe; 
	this.nbrSommets = vueGraphe.getGraphe().ordre();
	this.sommets = BoiteDistribue.tri(vueGraphe.getGraphe().sommets(),this.nbrSommets);
	this.nomDesSommets = new String[this.nbrSommets];
	this.mainPane = new JPanel();
	this.mainPane.setLayout(new BorderLayout());
	this.mainPane.setPreferredSize(new Dimension(400,150));
	this.ajouterBoutons();
    }
    
    //Methodes
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	this.dialog.pack();
	this.dialog.setVisible(true);
	this.dialog.setLocationRelativeTo(parent);
    }
    
    
    /**
     * Ajoute les boutons en bas de la boite. Suivant "est_editable", 
     * certains d'entre eux seront actifs ou non.                     
     */
    public void ajouterBoutons() {
	Sommet unSommet;

	this.buttonPane = new JPanel(new FlowLayout());
	this.labelPanel = new JPanel(new GridLayout(this.nbrSommets+1,0));
	this.toolBarPanel = new JPanel(new GridLayout(1,0));
	
	this.buttonOk = new JButton("Ok");
	this.buttonOk.addActionListener(this);
	
	this.buttonCancel = new JButton("Cancel");
	this.buttonCancel.addActionListener(this);
	


	JLabel blank = new JLabel("");
	this.hostLabel = new JLabel("Host");
	//portLabel= new JLabel("Port");
	this.localNodeLabel= new JLabel("LocalNode");
	this.toolBarPanel.add(blank);
	this.toolBarPanel.add(this.hostLabel);
	//toolBarPanel.add(portLabel);
	this.toolBarPanel.add(this.localNodeLabel);

	
	//ajout des champs de saisie de l'emplacement du serveur 
	//pour chaque noued du graphe
	this.nomSimuLabel = new JLabel("Console");
	this.nomSimuField = new JTextField("");
	//nomSimuPort = new JTextField("");
	this.nomSimuUrl = new JTextField("");
		
	this.labelPanel.add(this.nomSimuLabel);
	this.labelPanel.add(this.nomSimuField);
	//labelPanel.add(nomSimuPort);
	this.labelPanel.add(this.nomSimuUrl);
	
	
	int i=0;

	while(this.sommets.hasMoreElements()){
	    unSommet = (Sommet)this.sommets.nextElement();
	    String identite = unSommet.getSommetDessin().getEtiquette();
	    JLabel label = new JLabel("Node "+identite);
	    //label.setPreferredSize(new Dimension (80,20));
	    JTextField texte = new JTextField("");
	    //texte.setPreferredSize(new Dimension (80,20));
	    //JTextField lePort = new JTextField("");
	    //lePort.setPreferredSize(new Dimension (80,20));
	    JTextField urlName = new JTextField("");
		    
	    this.labelPanel.add(label);
	    this.labelPanel.add(texte);
	    //labelPanel.add(lePort);
	    this.labelPanel.add(urlName);
	    this.nomDesSommets[i] = identite;
	    i++;
	}

	
	this.buttonPane.add(this.buttonOk);
	this.buttonPane.add(this.buttonCancel);    
	
	this.scroller = new JScrollPane(this.labelPanel);
	
	this.scroller.setOpaque(true);
	this.mainPane.add(this.toolBarPanel,BorderLayout.NORTH);
	this.mainPane.add(this.scroller,BorderLayout.CENTER);
	this.mainPane.add(this.buttonPane,BorderLayout.SOUTH);
	this.dialog.getContentPane().add(this.mainPane,BorderLayout.CENTER);
    }
    
    //retourne la liste des serveurs saisies par l'utilisateur 
    public LocalNodeTable getParameter(){
	Hashtable tmp = new Hashtable();
	LocalNodeTable lnt = new LocalNodeTable();
	int j = 0 ;
	for(int i=4; i <= 3*this.nbrSommets+1; i++){
	    Component component = this.labelPanel.getComponent(i);
	    Component componentUrl = this.labelPanel.getComponent(i+1);
	    if ((component instanceof JTextField) && (componentUrl instanceof JTextField)){
		try{
		    JTextField texteField = (JTextField)component;
		    JTextField texteFieldUrl = (JTextField)componentUrl;


		    String server = texteField.getText();
		    String url = texteFieldUrl.getText();
		    //si un noeud n'est pas assigne a un NoeudLocal
		    if (url.equals("")){
			//la machine du noeud concerne contient un noeud local
			if (lnt.containsHost(server)) {
			    //on assigne ce noeud au noeud local deja declare
			    lnt.addToLocalNode(server,new Integer(j));
			} else {
			    //sinon on sauvegarde le noeud
			    //si ! ( il existe d'autres noeud assigne a la machine sans Noeud locaux)
			    if(!tmp.containsKey(server)){
				//on sauvegarde la nouvelle netree
				Vector v = new Vector();
				v.addElement(new Integer(j));
				tmp.put(server,v);
			    }				
			    else 
				// sinon on sauvegarde le nouveau noeud avec les ancien
				((Vector)tmp.get(server)).addElement(new Integer(j));
			}
		    } else {
			//sinon : on lit un noeud local
			//on rajoute cette information a lnt
			lnt.addLocalNode(server,url,new Integer(j));
			//On regarde dans les noeuds sauvegarde sans noeud local
			//si on trouve des noeuds assignee a la machine du noeud courant
			//on les rajoutent
			if (tmp.containsKey(server)) {
			    Vector nodesWithoutLocalNode = (Vector)tmp.remove(server);
			    lnt.addLocalNode(server,url,nodesWithoutLocalNode);
			}
		    }
		    j++;
			    
		} catch (Exception e){
		    System.out.println("Erreur dans BoiteDistribue : "+e);
		}
	    }
	}
	if (!tmp.isEmpty()) {
	    Enumeration e = tmp.keys();
	    while (e.hasMoreElements()){
		String elt = (String) e.nextElement(); 
		Vector v = (Vector)tmp.get(elt);
		lnt.addLocalNode(elt,elt,v);
	    }
	}
	lnt.print();
	return lnt;
    }
    

    public String getSimulatorHost() {
	Component component = this.labelPanel.getComponent(1);
	JTextField texte = (JTextField)component;
	return texte.getText();
    }

    public String getSimulatorUrl() {
	Component component = this.labelPanel.getComponent(2);
	JTextField texte = (JTextField)component;
	String textValue = texte.getText();
	if (textValue.equals("") | (textValue == null))
	    return this.DEFAULT_URL_FOR_SIMULATOR;
	else
	    return textValue;
    }
    
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == this.buttonOk) {
	    this.parent.setNetworkParam(this.getParameter(),this.getSimulatorHost(),this.getSimulatorUrl());
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
	
	if(e.getSource() == this.buttonCancel) {
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
    }

    public final static Enumeration tri(Enumeration v_enum,int taille){

	Vector triSommet = new Vector(taille);
	
	while(v_enum.hasMoreElements()){
	    Sommet unSommet = (Sommet)v_enum.nextElement();
	    String identite = unSommet.getSommetDessin().getEtiquette();
	    Integer identiteInteger = new Integer(identite);

	    if (triSommet.isEmpty()){
		triSommet.add(0,unSommet);
	    } else {
		boolean bool = true;
		int end =0;
		for(int i=0;i<triSommet.size();i++){
		    if(bool){
			Integer tmp = new Integer(((Sommet)triSommet.elementAt(i)).getSommetDessin().getEtiquette());
			if((tmp.compareTo(identiteInteger))>0){
			    triSommet.insertElementAt(unSommet,i);
			    bool=false;
			}			    
		    }
		    end = i;
		}
		if(bool){
		    triSommet.add(end+1,unSommet);
		}
	    }
	}
	return triSommet.elements();
    }

    /** Retourne le JDialog. */
    public JDialog dialog() {
	return this.dialog;
    }
    
}











