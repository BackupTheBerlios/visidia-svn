package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import javax.swing.*;

/**
 * Cette classe permet de cr�er des boites de dialogue simples :    
 * elles disparaissent quand l'utilisateur clique sur un de leurs   
 * boutons.                                                         
 */

public class Boite extends JOptionPane {
  
  /** La fen�tre parente : la boite sera centr�e par rapport � cette fen�tre.*/
  protected JFrame parent;

  /** Le titre de la boite */
  protected String title;

  protected static Object[] DISMISS_BUTTON = {"Dismiss"};
  protected static Object[] OK_CANCEL_BUTTONS = {"Ok", "Cancel"};

  protected static int DISMISS_OPTION = DEFAULT_OPTION;

  /** Construit une boite de type <b>type</b>, centr�e sur <b>parent</b>, et dont le titre sera <b>titre</b>
    * <br>Le constructeur n'affiche pas la boite.
    * @param type Un entier qui repr�sente le type de la boite.
    * Cet entier peut prendre les valeurs suivantes :
    * <ul>
    * <li> <b>DISMISS_OPTION</b> : la boite aura un seul bouton "dismiss" </li>
    * <li> <b>OK_CANCEL_OPTION</b> : la boite aura deux boutons : "ok" et "cancel" </li>
    * </ul>
      **/

  public Boite(JFrame parent,
	       String title,
	       int type) {
    super();
    this.title = title;
    this.parent = parent;

    if (type == OK_CANCEL_OPTION) {
      setOptions(OK_CANCEL_BUTTONS);    
    } else {
      setOptions(DISMISS_BUTTON);
    }

    setOptionType(type);
    setMessageType(PLAIN_MESSAGE);

  }


  /** Construit une boite de type OK_CANCEL (deux boutons), centr�e sur <b>parent</b>, et dont le titre sera <b>titre.</b>
    * <br>Les labels par d�faut des boutons sont remplac�s par ceux pass�s en arguments.
    * <br>Le constructeur n'affiche pas la boite.*/
  public Boite(JFrame parent,
	       String title,
	       String labelGauche,
	       String labelDroite) {
    super();
    this.title = title;
    this.parent = parent;
    Object[] labels = {labelGauche, labelDroite};
    setOptions(labels);

    setOptionType(OK_CANCEL_OPTION);
    setMessageType(PLAIN_MESSAGE);
    
  }

    /*
     * Cette m�thode permet de cr�er l'int�rieur de la boite.        
     * Elle est red�finie dans toutes les sous_classes de Boite.<br>  
     * Le JComponent retourn�, qui peut contenir n'importe quel      
     * composant Swing ou AWT, sera ajout� dans la boite, au dessus  
     * des boutons correspondant au type de boite choisi.            
     * Si des boutons sont ajout�s � ce composant,                   
     * les appels de m�thodes associ�s � l'action sur ces boutons    
     * peuvent �tre faits gr�ce aux m�thodes de l'interface          
     * ActionListener.                                               
     */
  public JComponent createContent() {
    return new JPanel();
  }
  
    /**
     * Cette m�thode fait appel � <b>createContent</b> pour cr�er le 
     * contenu de la boite, et affiche ensuite la boite.             
     * Quand l'utilisateur  appuie sur un bouton de la boite,        
     * celle-ci dispara�t. Si le bouton utilis� est le bouton "ok",  
     * la m�thode <b>boutonOkAppuye</b> est appel�e.                 
     * Sinon, la m�thode <b>boutonCancelAppuye</b> est appel�e.      
     */
    public void showDialog() { 
	setMessage(createContent());
	
	createDialog(parent, title).show();
	
	String selectedButton = (String)getValue();
	
	if (selectedButton != null) {
	    if (selectedButton == "Ok") {
		this.boutonOkAppuye();
	    } else if (selectedButton == "Cancel") {
		this.boutonCancelAppuye();
	    }
	}
    }
    
    
    /**
     * Cette m�thode construit la boite de dialogue, et retourne le label 
     * du bouton sur lequel a cliqu� l'utilisateur.                       
     * @param closeable Si ce bool�en vaut VRAI, la fermeture de la boite 
     */
    public String dialogValue() { 
	setMessage(createContent());
	createDialog(parent, title).show();
	return (String)getValue();
    }
    
    
    /**
     * M�thode appel�e apr�s la disparition de la boite, si l'utilisateur a 
     * cliqu� sur le bouton "ok"                                            
     */
    public  void boutonOkAppuye() {
    }
    /**
     * M�thode appel�e apr�s la disparition de la boite, si l'utilisateur a 
     * cliqu� sur le bouton "Cancel" .                                        
     */
    public  void boutonCancelAppuye() {
    }
}










