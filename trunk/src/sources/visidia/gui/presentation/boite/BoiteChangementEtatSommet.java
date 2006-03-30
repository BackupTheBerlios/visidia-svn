package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.presentation.*;
import visidia.gui.donnees.*;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un    
 * ou de plusieurs sommets selectionne elle est appelee quand on ne    
 * selectionne que des sommets et qu'on appui sur le bouton info     
*/
public class BoiteChangementEtatSommet
    implements ActionListener, ItemListener, VueEtatPanel
{
    /** La fenetre parent : la boite sera centree sur cette fenetre.*/
    protected FenetreDeSimulation parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;
    /** Le bouton Apply*/
    protected JButton buttonApply;
    /** the button for changing the algorithms */
    protected JButton buttonChange;
    /** The label for displaying the algorithm used */
    protected JLabel algoUsed;
    protected SommetDessin monSommet;
    protected int vertex_id = -1;
    protected EtatPanel etatPanel;
    /** a table for the state */
    protected JTable table = new JTable();

    protected JCheckBox but_drawMessage;
    protected boolean drawMessage= true;
    protected boolean drawMessageOldValue= drawMessage;
    //Constructeurs

    /**
     * Cree une nouvelle boite pour afficher les caractéristiques de
     * "un_objet".  Ces caractéristiques seront modifiables.
     */
    public BoiteChangementEtatSommet(FenetreDeSimulation parent, SommetDessin sommet) {
	this(parent, sommet, "Vertex properties state");
    }
    
    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public BoiteChangementEtatSommet(FenetreDeSimulation parent, SommetDessin sommet, 
				     String titre) {
    
    this.dialog = new JDialog(parent, titre);
    this.parent = parent;

    monSommet = sommet;
  
    vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();
    String algoString = new String();
    if (parent.getAlgorithms().getAlgorithm(vertex_id) == null) 
	algoString = "None";
    else algoString = parent.getAlgorithms().getAlgorithm(vertex_id).getClass().getName();
    algoUsed = new JLabel("Algorithm used : "+algoString);
    
    etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
    

    drawMessage = monSommet.getDrawMessage();
    drawMessageOldValue = drawMessage;
    but_drawMessage= new JCheckBox("Draw sending Message",drawMessage) ;
    but_drawMessage.addItemListener(this);

    Panel panelHaut = new Panel();
    panelHaut.setLayout(new BorderLayout());
    panelHaut.add(etatPanel, BorderLayout.NORTH);
    panelHaut.add(but_drawMessage, BorderLayout.SOUTH);
    
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane spane = new JScrollPane(table);
    
    JPanel panelCentre = new JPanel();
    panelCentre.setLayout(new BorderLayout());
    panelCentre.add(spane, BorderLayout.NORTH);
    panelCentre.add(algoUsed, BorderLayout.CENTER);
    
    setProperties((monSommet.getStateTable()));
       
    dialog.getContentPane().setLayout(new BorderLayout());
    //dialog.getContentPane().add(etatPanel, BorderLayout.NORTH);
    dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
    dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
    dialog.setSize(400,200);
    
    ajouterBoutons();
    
  }

    //Methodes  
    
       
  /** setting the state */
  public void setProperties(Hashtable props){
  	table.setModel(new PropertyTableModel(props));}  
    
  /** Affiche la boite et la centre par rapport a "parent".*/
  public void show(Frame parent) {
    dialog.pack();
    dialog.show();
    dialog.setLocationRelativeTo(parent);
  }
  
  /** Ajoute un bouton nomme "label" au panel "pane" */
  public JButton addButton(JPanel pane, String label) {
    JPanel tmp = new JPanel(new FlowLayout());
    JButton button = new JButton(label);
    tmp.add(button);
    button.setSize(button.getMinimumSize());
    pane.add(tmp);
    pane.add(Box.createRigidArea(new Dimension(0, 5)));
    return button;
  }
  
 
  /** Ajoute les boutons en bas de la boite.*/
  public void ajouterBoutons() {
    JPanel buttonPane = new JPanel(new FlowLayout());
    
    buttonOk = new JButton("Ok");
    buttonOk.addActionListener(this);
    
    buttonCancel = new JButton("Cancel");
    buttonCancel.addActionListener(this);

   
    buttonApply = new JButton("Apply");
    buttonApply.addActionListener(this);
    
    
    buttonPane.add(buttonOk);
    buttonPane.add(buttonCancel);    
    buttonPane.add(buttonApply);
    buttonApply.setEnabled(true);
    dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
  }
  

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == buttonOk) {
      try {
        buttonOk();
        dialog.setVisible(false);
        dialog.dispose();
      } catch(NumberFormatException exception) {
        StringTokenizer st =
          new StringTokenizer(exception.getMessage(), "\n");
        int nb_lignes = st.countTokens();
  	String message = new String();
        for(int i = 0; i < nb_lignes; i++)
          message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
      }
    }
    if(e.getSource() == buttonApply) {
      try {
	buttonOk();
	parent.repaint();
      } catch(NumberFormatException exception) {
	StringTokenizer st =
	  new StringTokenizer(exception.getMessage(), "\n");
	int nb_lignes = st.countTokens();
	String message = new String();
	for(int i = 0; i < nb_lignes; i++)
	  message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
	}
    }
    if(e.getSource() == buttonCancel) {
      dialog.setVisible(false);
      dialog.dispose();
    }
  }

    //Implementation de VueEtatPanel
    public void elementModified(String s){
	elementModified();
    }
    
    public void elementModified(){
	PropertyTableModel mod =(PropertyTableModel)table.getModel();
	mod.putProperty("label",etatPanel.ardoise().donneEtat());
    
	if(drawMessage)
	    mod.putProperty("draw messages","yes");
	else
	    mod.putProperty("draw messages","no");
    }
      
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	String etat = etatPanel.ardoise().donneEtat();
	PropertyTableModel mod =(PropertyTableModel)table.getModel();
	int nbRows = mod.getRowCount();
	monSommet.setEtat(etat);
	monSommet.setDrawMessage(drawMessage);

	try{
	    for (int i=0;i<nbRows;i++){
		table.editCellAt(i,2); // read the new values edited
		monSommet.setValue((String)mod.getValueAt(i,0),mod.getValueAt(i,2));
	    }
	}catch(Exception exc){System.out.println(" Problem in Box : "+exc);}
    

	//if(drawMessageOldValue != drawMessage){
  	    parent.nodeStateChanged(Integer.valueOf(monSommet.getEtiquette()).intValue(), mod.getProperties());
	    //}

	parent.simulationPanel().repaint();
    
    }


  /** Retourne le JDialog. */
  public JDialog dialog() {
    return dialog;
  }

    public void itemStateChanged(ItemEvent evt) {
	
	if((JCheckBox)evt.getSource() == but_drawMessage){
	    drawMessage = !drawMessage;
	    elementModified();
	}
    }
    
}

