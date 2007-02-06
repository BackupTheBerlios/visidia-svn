package visidia.algo;

import visidia.misc.*;
import visidia.algo.SynchroAlgo;
/**
 *Fonction qui prend en entree un ensemble de regles de reecriture et 
 *fait tourner ces regles sur le graphe.
 */

import java.util.Vector;
public class AlgoRule extends SynchroAlgo {
    public Vector rule; //SimpleRule
    private String finalState;
    protected boolean[] marquage;

    public AlgoRule(Vector r){   //SimpleRule
	rule = r;
	
    }
    
    public void init(){

	int arity=getArity();
	marquage = new boolean[arity];
	for(int i=0; i< arity;i++)
	    marquage[i]=false;
	while(true){
	    
	    
	    //synchro
	 
	    int neighbour = super.synchronization();
	    if(!this.isFinished()){		
		//choix du chef
		int mychoice = SynchronizedRandom.nextInt();
		sendTo(neighbour,new IntegerMessage(new Integer(mychoice)));
		IntegerMessage ccm=(IntegerMessage )receiveFrom(neighbour,new IntegerMessageCriterion());
		int compare= ccm.value();
		System.out.println(ccm);
		//si je suis le chef
		if( mychoice > compare ) {
		    //echange des etats
		    String hisState;
		    Message msg=receiveFrom(neighbour,new StringMessageCriterion());
		    StringMessage smsg = (StringMessage) msg;
		    hisState = smsg.data();
		    
		    //application de la regle
		    Arrow a = new Arrow(getState(),hisState);
		    Arrow after;
		    int longueur = rule.size();
		    System.out.println(longueur);
		    int i;
		    //parcourir toute la liste des rules
		    for( i=0; i < longueur; i++){
			System.out.println("Regle "+i);
			SimpleRule r = (SimpleRule) rule.elementAt(i);
			    
			if( r.isApplicable(a)){
			    after =r.apply(a);
				//marquage de l arete
			    if(marquage[neighbour] == false)
				marquage[neighbour] = after.isMarked;
			    if(a.isMarked)
				setDoorState(new MarkedState(true),neighbour);
			    
			    setState(after.left);
			    sendTo(neighbour,new ArrowMessage(after));
			    System.out.println(hisState);
			    break;
			}
		    }
		    if(i ==longueur){
			System.out.println("aucune regle applicable");
			sendTo(neighbour,new ArrowMessage(a));
		    }

		}
		else {
		    sendTo(neighbour, new StringMessage(getState()));
		    Message msg=receiveFrom(neighbour,new ArrowMessageCriterion()); 
		    ArrowMessage smsg =(ArrowMessage)msg;
		    Arrow arete = smsg.data();
		    setState(arete.right);
		    //marquage de l arete;
		    
		    if(marquage[neighbour]==false)
			marquage[neighbour]=arete.isMarked;
		    if(arete.isMarked)
			setDoorState(new MarkedState(true),neighbour);
		}
	    }
	}

    }

	
    


    /**
     *retourne  <code>true</code> si le sommet est dans l'etat final.
     */
    public boolean isFinished(){
	return false;
    }
    public Object clone(){
	return new AlgoRule(rule);

    }

    public String getState(){
	return (String) getProperty("label");
    }

    public void setState(String newState){
	putProperty("label", newState);
    }


}






