
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
	    generateGridGraph(w,h,spacing);
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

	for(int i = 0; i < n; i++){
	    System.out.println("edge [");
	    System.out.println("source "+i);
	    System.out.println("target "+(i+1)%n);
	    System.out.println("] ");
	}

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
    
    public static void usage(){
	System.out.println("usage: java GraphGenerator {cyclic rayon n} | {grid w h spacing}");
    }
}



