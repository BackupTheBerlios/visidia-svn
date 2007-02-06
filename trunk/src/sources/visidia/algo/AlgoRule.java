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
    protected boolean[] marquage;

    public AlgoRule(Vector r){   //SimpleRule
	this.rule = r;
	
    }
    
    public void init(){

	int arity=this.getArity();
	this.marquage = new boolean[arity];
	for(int i=0; i< arity;i++)
	    this.marquage[i]=false;
	while(true){
	    
	    
	    //synchro
	 
	    int neighbour = super.synchronization();
	    if(!this.isFinished()){		
		//choix du chef
		int mychoice = SynchronizedRandom.nextInt();
		this.sendTo(neighbour,new IntegerMessage(new Integer(mychoice)));
		IntegerMessage ccm=(IntegerMessage )this.receiveFrom(neighbour,new IntegerMessageCriterion());
		int compare= ccm.value();
		System.out.println(ccm);
		//si je suis le chef
		if( mychoice > compare ) {
		    //echange des etats
		    String hisState;
		    Message msg=this.receiveFrom(neighbour,new StringMessageCriterion());
		    StringMessage smsg = (StringMessage) msg;
		    hisState = smsg.data();
		    
		    //application de la regle
		    Arrow a = new Arrow(this.getState(),hisState);
		    Arrow after;
		    int longueur = this.rule.size();
		    System.out.println(longueur);
		    int i;
		    //parcourir toute la liste des rules
		    for( i=0; i < longueur; i++){
			System.out.println("Regle "+i);
			SimpleRule r = (SimpleRule) this.rule.elementAt(i);
			    
			if( r.isApplicable(a)){
			    after =r.apply(a);
				//marquage de l arete
			    if(this.marquage[neighbour] == false)
				this.marquage[neighbour] = after.isMarked;
			    if(a.isMarked)
				this.setDoorState(new MarkedState(true),neighbour);
			    
			    this.setState(after.left);
			    this.sendTo(neighbour,new ArrowMessage(after));
			    System.out.println(hisState);
			    break;
			}
		    }
		    if(i ==longueur){
			System.out.println("aucune regle applicable");
			this.sendTo(neighbour,new ArrowMessage(a));
		    }

		}
		else {
		    this.sendTo(neighbour, new StringMessage(this.getState()));
		    Message msg=this.receiveFrom(neighbour,new ArrowMessageCriterion()); 
		    ArrowMessage smsg =(ArrowMessage)msg;
		    Arrow arete = smsg.data();
		    this.setState(arete.right);
		    //marquage de l arete;
		    
		    if(this.marquage[neighbour]==false)
			this.marquage[neighbour]=arete.isMarked;
		    if(arete.isMarked)
			this.setDoorState(new MarkedState(true),neighbour);
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
	return new AlgoRule(this.rule);

    }

    public String getState(){
	return (String) this.getProperty("label");
    }

    public void setState(String newState){
	this.putProperty("label", newState);
    }


}






