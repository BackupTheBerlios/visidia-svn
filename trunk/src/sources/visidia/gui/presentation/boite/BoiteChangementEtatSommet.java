package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import visidia.gui.donnees.PropertyTableModel;
import visidia.gui.donnees.TableCouleurs;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;

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
    protected boolean drawMessageOldValue= this.drawMessage;
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

    this.monSommet = sommet;
  
    this.vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();
    String algoString = new String();
    if (parent.getAlgorithms().getAlgorithm(this.vertex_id) == null) 
	algoString = "None";
    else algoString = parent.getAlgorithms().getAlgorithm(this.vertex_id).getClass().getName();
    this.algoUsed = new JLabel("Algorithm used : "+algoString);
    
    this.etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
    this.etatPanel.ardoise().changerEtat(this.monSommet.getEtat());    

    this.drawMessage = this.monSommet.getDrawMessage();
    this.drawMessageOldValue = this.drawMessage;
    this.but_drawMessage= new JCheckBox("Draw sending Message",this.drawMessage) ;
    this.but_drawMessage.addItemListener(this);

    Panel panelHaut = new Panel();
    panelHaut.setLayout(new BorderLayout());
    panelHaut.add(this.etatPanel, BorderLayout.NORTH);
    panelHaut.add(this.but_drawMessage, BorderLayout.SOUTH);
    
    this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane spane = new JScrollPane(this.table);
    
    JPanel panelCentre = new JPanel();
    panelCentre.setLayout(new BorderLayout());
    panelCentre.add(spane, BorderLayout.NORTH);
    panelCentre.add(this.algoUsed, BorderLayout.CENTER);
    
    this.setProperties((this.monSommet.getStateTable()));
       
    this.dialog.getContentPane().setLayout(new BorderLayout());
    //dialog.getContentPane().add(etatPanel, BorderLayout.NORTH);
    this.dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
    this.dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
    this.dialog.setSize(400,200);
    
    this.ajouterBoutons();
    
  }

    //Methodes  
    
       
  /** setting the state */
  public void setProperties(Hashtable props){
  	this.table.setModel(new PropertyTableModel(props));}  
    
  /** Affiche la boite et la centre par rapport a "parent".*/
  public void show(Frame parent) {
    this.dialog.pack();
    this.dialog.show();
    this.dialog.setLocationRelativeTo(parent);
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
    
    this.buttonOk = new JButton("Ok");
    this.buttonOk.addActionListener(this);
    
    this.buttonCancel = new JButton("Cancel");
    this.buttonCancel.addActionListener(this);

   
    this.buttonApply = new JButton("Apply");
    this.buttonApply.addActionListener(this);
    
    
    buttonPane.add(this.buttonOk);
    buttonPane.add(this.buttonCancel);    
    buttonPane.add(this.buttonApply);
    this.buttonApply.setEnabled(true);
    this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
  }
  

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == this.buttonOk) {
      try {
        this.buttonOk();
        this.dialog.setVisible(false);
        this.dialog.dispose();
      } catch(NumberFormatException exception) {
        StringTokenizer st =
          new StringTokenizer(exception.getMessage(), "\n");
        int nb_lignes = st.countTokens();
  	String message = new String();
        for(int i = 0; i < nb_lignes; i++)
          message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(this.parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
      }
    }
    if(e.getSource() == this.buttonApply) {
      try {
	this.buttonOk();
	this.parent.repaint();
      } catch(NumberFormatException exception) {
	StringTokenizer st =
	  new StringTokenizer(exception.getMessage(), "\n");
	int nb_lignes = st.countTokens();
	String message = new String();
	for(int i = 0; i < nb_lignes; i++)
	  message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(this.parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
	}
    }
    if(e.getSource() == this.buttonCancel) {
      this.dialog.setVisible(false);
      this.dialog.dispose();
    }
  }

    //Implementation de VueEtatPanel
    public void elementModified(String s){
	this.elementModified();
    }
    
    public void elementModified(){
	PropertyTableModel mod =(PropertyTableModel)this.table.getModel();
	mod.putProperty("label",this.etatPanel.ardoise().donneEtat());
    
	if(this.drawMessage)
	    mod.putProperty("draw messages","yes");
	else
	    mod.putProperty("draw messages","no");
    }
      
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	String etat = this.etatPanel.ardoise().donneEtat();
	PropertyTableModel mod =(PropertyTableModel)this.table.getModel();
	int nbRows = mod.getRowCount();
	this.monSommet.setEtat(etat);
	this.monSommet.setDrawMessage(this.drawMessage);

	try{
	    for (int i=0;i<nbRows;i++){
		this.table.editCellAt(i,2); // read the new values edited
		this.monSommet.setValue((String)mod.getValueAt(i,0),mod.getValueAt(i,2));
	    }
	}catch(Exception exc){System.out.println(" Problem in Box : "+exc);}
    

	//if(drawMessageOldValue != drawMessage){
  	    this.parent.nodeStateChanged(Integer.valueOf(this.monSommet.getEtiquette()).intValue(), mod.getProperties());
	    //}

	this.parent.simulationPanel().repaint();
    
    }


  /** Retourne le JDialog. */
  public JDialog dialog() {
    return this.dialog;
  }

    public void itemStateChanged(ItemEvent evt) {
	
	if((JCheckBox)evt.getSource() == this.but_drawMessage){
	    this.drawMessage = !this.drawMessage;
	    this.elementModified();
	}
    }
    
}

