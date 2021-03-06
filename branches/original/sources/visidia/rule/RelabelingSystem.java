package visidia.rule;
import visidia.simulation.synchro.SynCT;
import visidia.misc.*;
import visidia.simulation.*;
import visidia.rule.*;

import visidia.algo.*;
import java.util.*;
import java.io.Serializable;
//mod
/**
    * Relabeling Systems 
    */
public class RelabelingSystem  implements Serializable {
    protected String description;
    protected MyVector rules;
    public RSOptions userPreferences;
/**
    * default constructor.
    */

    public RelabelingSystem()
    {
	this.rules = new MyVector();
	userPreferences = new RSOptions();
    }
    /**
    * constructor from a collection of rules. Options are affected to default values defined in class RSOptions.
    * @param rulesCollection the collection of rules.
    */
    public RelabelingSystem(Collection rulesCollection)
    {
	this.rules = new MyVector(rulesCollection);
	userPreferences = new RSOptions();
    }
/**
    *sets the options.
    * @param opt the object describing options.
    */
    public void setOptions(RSOptions opt)
    {
	userPreferences = opt;
    }
    /* restaurer les parametres par defaut */
   /**
    *restore the default options.
    */
    public void resetOptions()
    {
	userPreferences = new RSOptions();
    }
/**
    *this methode create and add the symetric of a simple Rule (if not exists).
it respects caracteristics of synchronisation algorithm on use. 
    * @param synType indicates the synchronisation algorithm to respect.
    * @return the number of rules dupplicated.
    */
    public int dupplicateSimpleRules(int synType)
    {
	Rule r;
	int k;
	int i = 0;
	Vector dup = new Vector();
	int numRules = rules.count();
	if (synType != SynCT.LC1){
	    while (i < numRules){
		Rule r1 =(Rule) rules.get(i); 
		k = r1.defaultSynchDegree();
		if (r1.isSimpleRule() && (k == SynCT.RDV) || (k==SynCT.RDV_LC1)) {
		    r = r1.inverseSimpleRule();
		    if (rules.contains(r) == -1) {
			rules.add(r);
			dup.add(new Integer(i));
		    }
		}
		i++;
	    }
	}
	//System.out.println("-->Les Regles simples ayant ete  Duppliques sont :"+dup);
	return dup.size();
    }
    /**
    * this methode returns an integer RDV LC1 RDV_LC1 or LC2, indicating witch
synchronisation algorithms are supported by this relabeling system.
    *
    * @return RDV if only RDV is possible (resp LC1, LC2)
    *         RDV_LC1 if both RDV_LC1 are possibles
    *         the LC2 algorithm is supposed to be acceptable.
    */
    public int defaultSynchronisation(){
	boolean rdvposs = true;
	boolean lc1poss = true;
	int i=0;
	while(i < rules.count()){
	    
	    int k = ((Rule) rules.get(i)).defaultSynchDegree();
	    //System.out.println("rule num "+i+" required syn --->"+k);
	    switch(k)
		{
		case SynCT.LC2:
		    return SynCT.LC2;
		case SynCT.LC1:
		    rdvposs = false;
		    break;
		case SynCT.RDV:
		    lc1poss= false;
		case SynCT.RDV_LC1:
		    break;
		}
	    if(!lc1poss && !rdvposs)
		return SynCT.LC2;
	    i++;
	}
	if(rdvposs && ! lc1poss)
	    return SynCT.RDV;
	if(lc1poss && !rdvposs)
	    return SynCT.LC1;
	if(lc1poss && rdvposs)
	    return SynCT.RDV_LC1;
	else
	    return SynCT.LC2;
    }
/**
    * returns the options.
    * @return options.
    */
    public RSOptions getOptions()
    {
	return (RSOptions)userPreferences.clone();
    }
    public String toString()
    {
	return "{\n< RELABELING SYSTEM >\n"+rules.toString()+"\n"+"< End of RELABELING SYSTEM >}";
    }
/**
    * add a rule to the relabeling system.
    * @param r the rule to add.
    */
    public void addRule(Rule r){
	this.rules.add(r);
    }
/**
    *add a Global End rule to the relabeling system.
    * @param r the rule to add.
    */
    public void addGlobEndRule(Rule r){
      Rule r2 = (Rule) r.clone();
      r2.setType(SynCT.GLOBAL_END);
      this.addRule(r2);
    }
    /**
    * return an iterator of rules.
    */
    public Iterator getRules(){
	return rules.iterator();
    }
    /**
    * return the rule at position i.
    * @param i position
    * @return the rule.
    */
    public Rule getRule(int i){
	return (Rule)((Rule) rules.get(i)).clone();
    }
/**
    * clones the relabeling system (including options).
    * @return 
    */
    public Object clone(){
	RelabelingSystem rs = new RelabelingSystem(rules.cloneRules());
	rs.setOptions(this.userPreferences);
	rs.setDescription(getDescription());
	return rs;
    }
    /**
    *set a help text concerning the relabeling system.
    * @param txt 
    */
    public void setDescription(String txt) {
	description = txt;
    }
/**
    * get a help.
    * @return the help text.
    */
    public String getDescription() {
	return description;
    }

/**
    * check if any rule is applicable to the context n. returns the position of the rule, -1 if no rule is applicable .
    * @param n a context.
    * @return the number of the rule, -1 if no rule is applicable.
    */
    public  int checkForRule(Star n)
    {
	int i = rules.count();
	int j = 0;
	int k=0;
	int l=0;
	boolean boucle = true;
	Star context = new Star();
	
	while(j < i ){
	    Star  neighbourhood = (Star)n.clone();
	    Rule r = (Rule)((Rule)rules.get(j)).clone();
	    Star s = (Star)((Star)r.befor()).clone();
	    //System.out.println("\n check for "+neighbourhood);
	    //   System.out.println("rule"+j+"with?="+r.withForbContexts()+"\n");
	    //System.out.println("contexts"+r.forbContexts+"\n");
	    if( neighbourhood.contains((Star)s.clone())){
		/* if rule contexte is convenable */
		if( r.withForbContexts()){
		    /* testing forbidden contexts */
		    MyVector v = new MyVector((Collection)r.forbContexts());
		    boucle = true;
		    l=0;
		    while( boucle && l < v.count())
			{
			    context =(Star)((Star) v.elementAt(l)).clone();
			    neighbourhood = (Star) n.clone();
			    //System.out.println("voisinage:"+neighbourhood);
			    //System.out.println("context"+context);
			    if(neighbourhood.contains((Star)context.clone())){
				//System.out.println("rejet");
				boucle = false;   
				l++;
			    }
			    else{
				l++;
			    }
			}
		    if(boucle){
			/* if ok */
			//System.out.println("rule "+j+" Accepted after examination of forbidden contexts");
			return j;
		    }
		    else{
			/* not ok -> try next rule */
			//	System.out.println("rule"+j+"refused because of forb context N "+ k);
			
			j++;
		    }
		}
		/* if there is no forbidden contexts */
		else{
		    return j;
		} 
	    }
	    else
		{
		    //System.out.println("next rule");
		    j++;
		}
	}
	//System.out.println("Neigbourhood"+neighbourhood+"  j="+j);
	return -1;
    }
}
