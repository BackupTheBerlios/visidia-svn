package visidia.rule;

import visidia.simulation.synchro.SynCT;
//import visidia.rule.*;
//import java.io.*;
import java.util.*;
import java.io.Serializable;

//mod

public class Rule implements Serializable {
    protected Star befor = new Star();
    protected Star after = new Star();
    protected MyVector forbContexts;
    protected int type = SynCT.GENERIC; //normal rule (default)
    protected boolean simpleRule;


    /**
    * default constructor. default values are defined in class: Star, MyVector
    * 
    */ 
    public Rule(){
	this(new Star(), new Star(), new MyVector());
	type = SynCT.GENERIC;
    }

    
    /**
    * a constructor a Rule without forbidden contexts.
    */  
    public Rule(Star b, Star a){
	this(b, a, new MyVector());
    }


    /**
    * 
    * @param b the star befor
    * @param a the star after
    * @param fc a MyVector of forbidden contexts.
    */  
    public Rule(Star b, Star a, MyVector fc){
	befor = b;
	after = a;
	forbContexts = fc;
	simpleRule = false;
    }
    

    /**
     *return the star befor
     */
    public Star befor(){
	return befor;
    }
    


    /**
     *return the star after.
     */
    public Star after(){
	return after;
    }
    


    /**
     *return the MyVector of forbidden contexts.
     */
    public MyVector forbContexts(){
	return forbContexts;
    }



    /**
    * this method check if the rule contains any forbidden contexts or no.
    * @return true if the rule contains any forbidden context, false otherweise.
    */  
    public boolean withForbContexts()
	{
	    return (this.forbContexts().count() > 0);
	}
    


    public String toString()
	{
	    return "\n<Rule>=\n  Bef= "+befor.toString()+"\n"+"  Aft= "+after.toString()+" \n  <Forbidden>"+forbContexts.toString()+"<End of Forbidden>\n<End of Rule>}";
	}
    


    /**
     * sets the type of the rule. possible values are defined in class SynCT.
     * no verification is done.
     */
    public void setType(int t){
	type = t;
    }
    


    /**
     *return the type of the rule.
     * possible types are defined in class SynCT.
     */

   public int getType(){
	return type;
    }


    /**
    * return true if the rule is simple, false otherweise. 
    */
    public boolean isSimpleRule() {
	return simpleRule;
    }
    

    /**
     * sets the kind of rule, tue if simple, false if not simple.
     */
    public void setSimpleRule(boolean b) {
	simpleRule = b;
    }
    

    /**
     * return true if the rules are equals.
     * warning: forbidden contexts are not compared. (out of model).
     *@param r  rule.
     */
    public boolean equals(Rule r) {
	if((this.befor.equals(r.befor())) && (this.after.equals(r.after())))
	    return true;
	return false;
    }
    


    /* returns rdv, lc1, or lc2 whenever needs RDV, LC1, or LC2 */
    /**
     * this methode returns an integer RDV LC1 RDV_LC1 or LC2,
     * indicating witch synchronisation algorithms are supported by
     * the rule.
     *
     * @return RDV if only RDV is possible (resp LC1, LC2).  RDV_LC1
     * if both RDV_LC1 are possibles.  the LC2 algorithm is supposed
     * to be acceptable.
     */
    public int defaultSynchDegree(){
	boolean rdvposs = true;
	boolean lc1poss = true;
	if(this.withForbContexts() || this.befor.arity() > 1) {
	    rdvposs = false;
	}
	
	Star a =(Star) befor.clone();
	Star b =(Star) after.clone();
	a.setCenterState(b.centerState());
	if(! a.containsLabels(b))//in fact it tests equality
	    {
		lc1poss=false;
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
     * this methode decides if the rule can be applied to the context neighbourhood.
     *@param neighbourhood the context.
     *@return true if rule is applicable, false if it isn't.
     */   
    
    public boolean isApplicableTo(Star neighbourhood){
       	int j = 0;
	int k=0;
	int l=0;
	boolean boucle = true;
	Star context = new Star();
	Star s = (Star)befor.clone();
	if (neighbourhood.contains(s)){
	    /* if rule contexte is convenable */
	    if(withForbContexts()){
		/* testing forbidden contexts */
		MyVector v = new MyVector((Collection) forbContexts());
		boucle = true;
		l=0;
		Star n = new Star();
		while( boucle && l < v.count())
		    {
			context =(Star)((Star) v.elementAt(l)).clone();
			n = (Star) neighbourhood.clone();
			
			if(n.contains((Star)context.clone())){
			    // System.out.println("rejet");
			    boucle = false;   
			    l++;
			}
			else{
			    l++;
			}
		    }
		if(boucle){
		    /* if ok */
		    // System.out.println("   rule "+j+" Accepted after examination of forbidden contexts");
		    return true;
		}
		else{
		    /* not ok -> try next rule */
		    //System.out.println("    rule"+j+"refused because of forb context N "+ k );
		    
		    return false;
		}
	    }
	    /* if there is no forbidden contexts */
	    else{
		return true;
	    } 
	}
	else
	    {
		return false;
	    }
    }



    /**
     * return the inverse of a simple rule.
     * exemple: A-N --> U-V {fc} becomes N-A --> V-U {fc}
     */	
    public Rule inverseSimpleRule(){
	Star b;
	Star a;
	Neighbour nb = befor.neighbour(0);
	Neighbour na = after.neighbour(0);
	b = new Star(nb.state(), 1);
	b.setState(0, new Neighbour(befor().centerState(), nb.mark(), nb.doorNum()));
	a = new Star(na.state(), 1);
	a.setState(0, new Neighbour(after().centerState(), na.mark(), na.doorNum()));
	Rule r = new Rule(b, a, (MyVector) forbContexts.clone()); 
	r.setSimpleRule(isSimpleRule());
	return r;
    }



    /**
     * clones the rule.
     */
    public Object clone(){
	Rule r = new Rule();
	r.befor =(Star) this.befor.clone();
	r.after =(Star) this.after.clone();
	r.forbContexts = new MyVector(forbContexts.size());
	r.forbContexts.addAll( (Collection)forbContexts.clone());
	r.type = this.type;
	r.setSimpleRule(isSimpleRule());
	return r;
    }


    /* Main de Test */
    static public void main(String args[]){
	//System.out.println("/********* Test of Rule ********/");
	Star befor = new Star(3);
	//System.out.println("___printing test___");
	//System.out.println("___ test of setState ___ ");
	Neighbour n1 = new  Neighbour("A", false);
	Neighbour n2 = new  Neighbour("A", false);
	Neighbour n3 = new  Neighbour("B", false);
	Neighbour n2bis = new  Neighbour("F", true);
	befor.setState(0,n1);
	befor.setState(1,n2);
	befor.setState(2,n3);
	//System.out.println("befor= "+befor);
	Star after = new Star(3);
	after.setState(0,n1);
	after.setState(1,n2bis);
	after.setState(2,n3);
	Star s3 = new Star(2);
       	MyVector fc = new MyVector();
	fc.add(s3);
	Rule r1 = new Rule(befor, after, fc);
	//System.out.println("Rule with forbidden context: "+r1);
	//System.out.println("? true= "+ r1.withForbContexts());
	//System.out.println("defaultSynch=2 ? "+r1.defaultSynchDegree());
	//System.out.println("Rule clone: "+r1.clone());
	r1 = new Rule(new Star("A",0),new Star("B",0));
	// test de def synchro
	//System.out.println("defaultSynch=0 ? "+r1.defaultSynchDegree());
	r1 = new Rule(new Star("A",0),new Star("B",0),fc);
	//System.out.println("defaultSynch=1 ? "+r1.defaultSynchDegree());
        befor = new Star("A",1);
	Neighbour nf = new  Neighbour("N", false);
	Neighbour mf = new  Neighbour("M", false);
	Neighbour mt = new  Neighbour("M", true);
	Neighbour at = new  Neighbour("A", true);
	
	befor.setState(0,nf);
	after = new Star("A",1);
	after.setState(0,mt);
        r1 = new Rule((Star) befor.clone(), (Star) after.clone());
	//System.out.println("defaultSynch=0 ? "+r1.defaultSynchDegree());

	befor = new Star("M",1);
	befor.setState(0,(Neighbour)nf.clone());
	after = new Star("M",1);
	after.setState(0,mt);
	r1 = new Rule(befor, after);
	//System.out.println("defaultSynch=0 ? "+r1.defaultSynchDegree());

	
	Star befor2 = new Star("M", 0);
	after = new Star("F",0);
	MyVector fc3 = new MyVector(3);
	fc3.add((Star)befor.clone());// M--N
	Star c2 = new Star("M",2);
	c2.setState(0,mt);
	c2.setState(1,mt);
	fc3.add(c2.clone());
	c2.setState(0,at);
	fc3.add(c2.clone());
	r1=new Rule(befor2,after,fc3);
	r1.setType(-4);// localEnd
	//System.out.println("defaultSynch=1 ? "+r1.defaultSynchDegree());

    }
    
}
