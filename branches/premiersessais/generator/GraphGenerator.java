import java.util.Vector;
import java.util.Random;
import java.lang.Math;

public class GraphGenerator {
    public static void main(String[] args) throws Throwable{
	if(args.length < 1){
	    usage();
	    return;
	}

	if(args[0].equals("cyclic")){
	    if(args.length < 3){
		usage();
		return;
	    }
	    double r = new Double(args[1]).doubleValue();
	    int n = new Integer(args[2]).intValue();
	    generateCyclicGraph(r,n);
	    return;
	}

	if(args[0].equals("grid")){
	    if(args.length < 4){
		usage();
		return;
	    }

	    int w = new Integer(args[1]).intValue();
	    int h = new Integer(args[2]).intValue();
	    int spacing = new Integer(args[3]).intValue();
	    generateGridGraph2(w,h,spacing);
	    return;
	}
	
	if(args[0].equals("Cnk")){
	    if(args.length < 4){
		usage();
		return;
	    }
	    
	    double r = new Double(args[1]).doubleValue();
	    int n = new Integer(args[2]).intValue();
	    int k = new Integer(args[3]).intValue();

	    if (2*k>n)
		k=n/2;
	    
	    generateCnkGraph(r,n,k);
	    return;
	   
	}
	
	if(args[0].equals("Tree")){
	        
	    if(args.length < 2){
		usage();
		return;
	    }
	    else {
		int arity;
		int n = new Integer(args[1]).intValue();
		
		if(args.length == 2)
		    arity=n/2;
		else
		    arity= new Integer(args[2]).intValue();
	    
		//System.err.println(arity);
		generateTreeGraph2(n,arity);
		return;
	    }
	}

	if(args[0].equals("DTree")){
	        
	    if(args.length < 2){
		usage();
		return;
	    }
	    else {
		int arity=new Integer(args[2]).intValue();
		int d = new Integer(args[1]).intValue();
		
		generateDTreeGraph(d,arity);
		return;
	    }
	}
	
	if(args[0].equals("Rand")){
	        
	    if(args.length < 1){
		usage();
		return;
	    }
	    else {
		int n=new Integer(args[1]).intValue();
		int d;
		
		if(args.length == 2)
		    d=-1;
		else
		    d= new Integer(args[2]).intValue();
		
		if (d>(n*n*(n-1)/2))
		    d=-1;
		
		generateRandomGraph(n,d);
		return;
	    }
	}
	
	if(args[0].equals("Comp")){
	    if(args.length < 1){
		usage();
		return;
	    }
	    
	    int n = new Integer(args[1]).intValue();
	    generateCompletGraph(n);
	    return;
	}

    }

    
    public static void generateCyclicGraph(double r, int n){
	System.out.println("graph [");

	double section = 2 * Math.PI / n;
	double arc = 0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + r * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + r * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}

	for(int i = 0; i < n-1; i++){
	    System.out.println("edge [");
	    System.out.println("source "+i);
	    System.out.println("target "+(i+1)%n);
	    System.out.println("] ");
	}
	System.out.println("edge [");
        System.out.println("source 0");
        System.out.println("target "+(n-1));
        System.out.println("] ");

	System.out.println("] ");
    }
    
    
    public static void generateGridGraph(int w, int h, int spacing){
	System.out.println("graph [");

	for(int i = 0, x = 20; i < w; i++, x+=spacing){
	    for(int j = 0, y = 20; j < h; j++, y+=spacing){
		System.out.println("node [");
		System.out.println("id "+(j+w*i));
		System.out.println("graphics [");
		System.out.println("x " + x);
		System.out.println("y " + y);
		System.out.println("] ");
		System.out.println("] ");
	    }
	}

	for(int i = 0; i < w; i++){
	    for(int j = 0; j < h - 1; j++){
	    System.out.println("edge [");
	    System.out.println("source "+(j + w*i));
	    System.out.println("target "+(j+1 + w*i));
	    System.out.println("] ");
	    }
	}

	for(int j = 0; j < h; j++){
	    for(int i = 0; i < w - 1; i++){
	    System.out.println("edge [");
	    System.out.println("source "+(j + w*i));
	    System.out.println("target "+(j + w*(i+1)));
	    System.out.println("] ");
	    }
	}

	System.out.println("] ");
    }
    

    public static void generateGridGraph2(int w, int h, int spacing){
	System.out.println("graph [");

	for(int i = 0, x = 20; i < w; i++, x+=spacing){
	    for(int j = 0, y = 20; j < h; j++, y+=spacing){
		System.out.println("node [");
		System.out.println("id "+(i+j*w));
		System.out.println("graphics [");
		System.out.println("x " + x);
		System.out.println("y " + y);
		System.out.println("] ");
		System.out.println("] ");
	    }
	}

	for(int i = 0; i < w-1; i++){
	    for(int j = 0; j < h; j++){
	    System.out.println("edge [");
	    System.out.println("source "+(i + w*j));
	    System.out.println("target "+(i+1 + w*j));
	    System.out.println("] ");
	    }
	}

	for(int j = 0; j < h-1; j++){
	    for(int i = 0; i < w; i++){
	    System.out.println("edge [");
	    System.out.println("source "+(i + w*j));
	    System.out.println("target "+(i + w*(j+1)));
	    System.out.println("] ");
	    }
	}

	System.out.println("] ");
    }
    
     public static void generateCnkGraph(double r, int n, int k){
	System.out.println("graph [");

	double section = 2 * Math.PI / n;
	double arc = 0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + r * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + r * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}

	for(int i = 0; i < n; i++)
	    for (int j=1;j<=k;j++){
		
		System.out.println("edge [");
		System.out.println("source "+i);
		System.out.println("target "+(i+j)%n);
		System.out.println("] ");
	    }
	
	System.out.println("] ");
     }

    public static void generateTreeGraph(int n,int arity){
	System.out.println("graph [");
	
	double section = 2 * Math.PI / n;
	double arc = 0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + 300.0 * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + 300.0 * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}
	
	Vector vFree=new Vector();
	Vector vTree=new Vector();
	
	int root=0,son;
	Random generator = new Random();
	for (int i=1;i<n;i++)
	    vFree.addElement(new Integer(i));
	
	vTree.addElement(new Integer(0));

	while (!vFree.isEmpty()){
	    int nextRoot,numberArity= Math.abs((generator.nextInt(arity+1)));

	    for (int i=0;(i<numberArity) && (!vFree.isEmpty());i++) {
		int cn= Math.abs((generator.nextInt()))% vFree.size();
		son=((Integer)vFree.elementAt(cn)).intValue();
		vFree.removeElementAt(cn);
		vTree.addElement(new Integer(son));

		System.out.println("edge [");
		System.out.println("source "+root);
		System.out.println("target "+son);
		System.out.println("] ");
	    }
	    nextRoot=Math.abs((generator.nextInt(vTree.size())));
	    root=((Integer)vTree.elementAt(nextRoot)).intValue();
	}
	
	System.out.println("] ");
    }
    

    public static void generateTreeGraph2(int n,int arity){
	int numArity[]=new int[n];
	
	System.out.println("graph [");
	
	double section = 2 * Math.PI / n;
	double arc = 0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + 300.0 * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + 300.0 * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}
	
	Vector vFree=new Vector();
	Vector vTree=new Vector();
	
	int root=0,son;
	Random generator = new Random();
	for (int i=1;i<n;i++) {
	    vFree.addElement(new Integer(i));
	    numArity[i]=arity;
	}
	
	vTree.addElement(new Integer(0));
	numArity[0]=arity;
	
	while (!vFree.isEmpty()){
	    int nextRoot,numberArity= Math.abs((generator.nextInt(arity+1)));

	    if (numArity[root]>=numberArity)
		for (int i=0;(i<numberArity) && (!vFree.isEmpty());i++) {
		    int cn= Math.abs((generator.nextInt()))% vFree.size();
		    son=((Integer)vFree.elementAt(cn)).intValue();
		    vFree.removeElementAt(cn);
		    vTree.addElement(new Integer(son));
		    numArity[root]--;
		    
		    System.out.println("edge [");
		    System.out.println("source "+root);
		    System.out.println("target "+son);
		    System.out.println("] ");
		}
	    
	    nextRoot=Math.abs((generator.nextInt(vTree.size())));
	    root=((Integer)vTree.elementAt(nextRoot)).intValue();
	}
	
	System.out.println("] ");
    }
    
    public static void generateDTreeGraph(int d,int arity){
	int n=(((int)Math.pow(arity-1,d+1) + (int)Math.pow(arity-1,d))-2)/(arity-2);
	
	//System.err.println(n);

	System.out.println("graph [");
	
	double section = 2 * Math.PI / n;
	double arc = 0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + 300.0 * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + 300.0 * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}
	
	Vector vFree=new Vector();
	Vector vTree=new Vector();
	
	int root=0,son;
	Random generator = new Random();
	for (int i=1;i<n;i++) {
	    vFree.addElement(new Integer(i));
	}
	
	vTree.addElement(new Integer(0));
	int n_arity=arity;
	
	while (vFree.size()!=0){
	    
	    for (int i=0;i<n_arity;i++) {
		int cn= Math.abs((generator.nextInt()))% vFree.size();
		
		son=((Integer)vFree.elementAt(cn)).intValue();
		vFree.removeElementAt(cn);
		vTree.addElement(new Integer(son));
		
		System.out.println("edge [");
		System.out.println("source "+root);
		System.out.println("target "+son);
		System.out.println("] ");
	    }
	    
	    vTree.removeElementAt(0);
	    root=((Integer)vTree.elementAt(0)).intValue();
	    n_arity=arity-1;
	}
	
	System.out.println("] ");
    }


    public static void generateRandomGraph(int n,int d){
	System.out.println("graph [");
	
	double section = 2 * Math.PI / n;
	double arc = 0;
	Vector neighbour[];

	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + 300.0 * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + 300.0 * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}
	
	Random generator = new Random();
	int numberEdge;

	if (d==-1)
	    numberEdge= (Math.abs(generator.nextInt( (n*n-3*n+2)/2 )))+n-1;
	else
	    numberEdge=d;

	//System.err.println(numberEdge);

	Vector vFree=new Vector();
	Vector vTree=new Vector();
	
	int root=0,son;
	
	neighbour=new Vector[n];
	
	for (int i=1;i<n;i++) {
	    vFree.addElement(new Integer(i));
	    neighbour[i]=new Vector();
	    neighbour[i].addElement(new Integer(i));
       	}

	neighbour[0]=new Vector();
	neighbour[0].addElement(new Integer(0));
	vTree.addElement(new Integer(0));
	
	while (!vFree.isEmpty()){
	    int nextRoot,numberArity= Math.abs(generator.nextInt((vFree.size()+1)));
	    
	    for (int i=0;(i<numberArity) && (!vFree.isEmpty());i++) {
		int cn= Math.abs((generator.nextInt()))% vFree.size();
		son=((Integer)vFree.elementAt(cn)).intValue();
		vFree.removeElementAt(cn);
		vTree.addElement(new Integer(son));
		neighbour[root].addElement(new Integer(son));
		neighbour[son].addElement(new Integer(root));
		numberEdge--;
		
		System.out.println("edge [");
		System.out.println("source "+root);
		System.out.println("target "+son);
		System.out.println("] ");
	    }
	    
	    nextRoot=Math.abs((generator.nextInt(vTree.size())));
	    root=((Integer)vTree.elementAt(nextRoot)).intValue();
	}

	//System.err.println(numberEdge);
	
	while (numberEdge>0){
	    root= Math.abs((generator.nextInt(n)));
	    son = Math.abs((generator.nextInt(n)));
	    
	    if (!neighbour[root].contains(new Integer(son))) {
		neighbour[root].addElement(new Integer(son));
		neighbour[son].addElement(new Integer(root));
		numberEdge--;
		
		System.out.println("edge [");
		System.out.println("source "+root);
		System.out.println("target "+son);
		System.out.println("] ");
	    }
	}
	
	System.out.println("] ");

    }

    public static void generateCompletGraph(int n){
	System.out.println("graph [");

	double section = 2 * Math.PI / n;
	double arc = 0,r=300.0;
	for(int i = 0; i < n; i++, arc += section){
	    System.out.println("node [");
	    System.out.println("id "+i);
	    System.out.println("graphics [");
	    System.out.println("x " + (50 + r * (1 + Math.cos(arc))));
	    System.out.println("y " + (50 + r * (1 + Math.sin(arc))));
	    System.out.println("] ");
	    System.out.println("] ");
	}

	for(int i = 0; i < n-1; i++)
	   for(int j = i+1; j < n; j++) {
	       System.out.println("edge [");
	       System.out.println("source "+i);
	       System.out.println("target "+j);
	       System.out.println("] ");
	   }
	
	System.out.println("] ");
    }
    
    public static void usage(){
	System.out.println("usage: java GraphGenerator {cyclic rayon n} | {grid w h spacing} | {Cnk rayon n k} | {Tree n [arity]} | {DTree H arity} | {Rand n [edges]} | {Comp n}");
    }
}



