package visidia.gui.presentation.boite;

import java.awt.*;
import javax.swing.*;

/**
 * Cette classe permet de creer des boites de dialogue simples :    
 * elles disparaissent quand l'utilisateur clique sur un de leurs   
 * boutons.                                                         
 */

public class Boite extends JOptionPane {
  
  /** La fenetre parente : la boite sera centree par rapport a cette fenetre.*/
  protected JFrame parent;

  /** Le titre de la boite */
  protected String title;

  protected static Object[] DISMISS_BUTTON = {"Dismiss"};
  protected static Object[] OK_CANCEL_BUTTONS = {"Ok", "Cancel"};

  protected static int DISMISS_OPTION = DEFAULT_OPTION;

  /** Construit une boite de type <b>type</b>, centree sur <b>parent</b>, et dont le titre sera <b>titre</b>
    * <br>Le constructeur n'affiche pas la boite.
    * @param type Un entier qui represente le type de la boite.
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


  /** Construit une boite de type OK_CANCEL (deux boutons), centree sur <b>parent</b>, et dont le titre sera <b>titre.</b>
    * <br>Les labels par defaut des boutons sont remplaces par ceux passes en arguments.
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
     * Cette methode permet de creer l'interieur de la boite.        
     * Elle est redefinie dans toutes les sous_classes de Boite.<br>  
     * Le JComponent retourne, qui peut contenir n'importe quel      
     * composant Swing ou AWT, sera ajoute dans la boite, au dessus  
     * des boutons correspondant au type de boite choisi.            
     * Si des boutons sont ajoutes a ce composant,                   
     * les appels de methodes associes a l'action sur ces boutons    
     * peuvent etre faits grace aux methodes de l'interface          
     * ActionListener.                                               
     */
  public JComponent createContent() {
    return new JPanel();
  }
  
    /**
     * Cette methode fait appel a <b>createContent</b> pour creer le 
     * contenu de la boite, et affiche ensuite la boite.             
     * Quand l'utilisateur  appuie sur un bouton de la boite,        
     * celle-ci disparait. Si le bouton utilise est le bouton "ok",  
     * la methode <b>boutonOkAppuye</b> est appelee.                 
     * Sinon, la methode <b>boutonCancelAppuye</b> est appelee.      
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
     * Cette methode construit la boite de dialogue, et retourne le label 
     * du bouton sur lequel a clique l'utilisateur.                       
     * @param closeable Si ce booleen vaut VRAI, la fermeture de la boite 
     */
    public String dialogValue() { 
	setMessage(createContent());
	createDialog(parent, title).show();
	return (String)getValue();
    }
    
    
    /**
     * Methode appelee apres la disparition de la boite, si l'utilisateur a 
     * clique sur le bouton "ok"                                            
     */
    public  void boutonOkAppuye() {
    }
    /**
     * Methode appelee apres la disparition de la boite, si l'utilisateur a 
     * clique sur le bouton "Cancel" .                                        
     */
    public  void boutonCancelAppuye() {
    }
}










