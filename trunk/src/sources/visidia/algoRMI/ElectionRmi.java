/**
 * Algorithme distribué d'election dans un graphe anonyme.
 * Marche bien pour les arbres,et les anneaux, et en général pour
 * les réseaux dont le diamètre (plus grand chemin) est inferieur au plus
 * petit cycle.Pour avoir un algorithme qui marche pour tout type de 
 * réseau, on doit pouvoir connaitre le degrès du réseau.
 **/


package visidia.algoRMI;
import visidia.simulation.*;
import  visidia.misc.*;
import java.util.Vector;
import java.util.Random;

public class ElectionRmi extends AlgorithmDist {


    /**
	 * 
	 */
	private static final long serialVersionUID = 8143775012864030440L;

	/**
     *Retourne <code>true</code> si le noeud est elu ou 
     *recoit un message de fin .
     **/
    private boolean isFinished(int ig){
	String mystate = getState();
	if(mystate.equals("G"))
	    return true;
	
	if(mystate.equals("F"))
	    return true;
	
        return false;	
    }


    /**
     *Algorithme d'election.L'algorithme implémente est celui de Lelanns,
     *avec quelques variantes.Tous les noeuds sont "candidats".Chacun 
     *tire un nombre au hasard,et fait circuler ce nombre dans le reseau,
     *ainsi que son identite (pour identifier le message).
     *Le noeud qui a tire le plus grand nombre sera elu.Si deux noeuds 
     *ont tire le meme nombre,on selectionne sur l'identite du noeud.
     */
    public void init(){
	//Amelioration rajouter un tableau pour tous les noeuds si N est connu 
	int arity = getArity();
	
	//je suis candidat
	setState("C");
	
	//choix d'un nombre au hasard
	int mynumber = (new Random()).nextInt()* getId().intValue();
	//j envoie mon tirage a tout mes voisins
	for(int i=0; i< arity; i++){
	    Vector vec = new Vector(2);
	    vec.add(getId());
	    vec.add(new Integer(mynumber));
	    sendTo(i,new VectorMessage((Vector)vec.clone()));
	}
    
	//debut de la procedure : 
	while( !isFinished(mynumber) ){
	    Door porte=new Door();
	    VectorMessage vmsg =(VectorMessage) receive(porte);
	    Vector data = vmsg.data();
	    int sender = porte.getNum();
	    
	    //message de fin ?
	    if(data.elementAt(0) instanceof Boolean){
		setState("F");
		for(int i=0;i<arity;i++){
		    if(i != sender)
			sendTo(i,new VectorMessage((Vector)data.clone()));
		}
		break;
	    }

	    int senderId = ((Integer)data.elementAt(0)).intValue();
	    int hisnumber =  ((Integer)data.elementAt(1)).intValue();

	    if(getState().equals("C")){
	    //mon message ? --> j'ai gagne
	    if(senderId == getId().intValue()){
		setState("G");
		// printStatistics();
		Vector v = new Vector(1);
		v.add(new Boolean(true));
		for(int i=0; i< arity; i++){
		    sendTo(i,new VectorMessage((Vector)v.clone()));
		}
	    }
	    // le nombre tire est plus grand que le mien -->j'ai perdu
	    else if(hisnumber > mynumber){
		setState("P");
		for(int i=0; i< arity; i++){
		    if(i != sender){
			Vector vec = new Vector(2);
			vec.add(new Integer(senderId));
			vec.add(new Integer(hisnumber));
			sendTo(i,new VectorMessage((Vector)vec.clone()));
		    }
		}
	    }
	    
	    //si le nombre est le meme -->selection sur le nodeId
	    else if(hisnumber == mynumber){
		if(getId().intValue()  > senderId){
		    setState("G");
		    // printStatistics();
		    Vector v = new Vector(1);
		    v.add(new Boolean(true));
		    for(int i=0; i< arity; i++){
			sendTo(i,new VectorMessage((Vector)v.clone()));
		    }
		}
		else {
		    setState("P");
		    for(int i=0; i< arity; i++){
			if(i != sender){
			    Vector vec = new Vector(2);
			    vec.add(new Integer(senderId));
			    vec.add(new Integer(hisnumber));
			    sendTo(i,new VectorMessage((Vector)vec.clone()));
			}
		    }
		}
	    }
	    }
	    else { //mon etat est "P"
    		for(int i=0; i< arity; i++){
		    if(i != sender){
			Vector vec = new Vector(2);
			vec.add(new Integer(senderId));
			vec.add(new Integer(hisnumber));
			sendTo(i,new VectorMessage((Vector)vec.clone()));
		    }
		}
	    }
      }
    }


    public String getState(){
	return (String) getProperty("label");
    }

    public void setState(String newState){
	putProperty("label", newState);
    }

    public Object clone(){
	return new ElectionRmi();
    }
    
}


