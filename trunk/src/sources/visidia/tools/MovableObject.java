package visidia.tools;

import java.awt.Point;

/**
*/
public class MovableObject {
	// segment end points
	double ax, ay, bx, by;
	    
    // the current position of this object on the straight
    private double x,y;
    
    // moving step width
    private double step;
    
    // units
    double ux,uy;
    
    /**
     */
    public MovableObject(Point a, Point b){
    	this(a,b,1);
    }
    
    
    public MovableObject(Point a, Point b, double step){
    	this.step = step;
    	
    	this.ax = a.getX(); this.ay = a.getY();
    	this.bx = b.getX(); this.by = b.getY();
    	
	this.x = this.ax; this.y = this.ay;
	
	double distance = a.distance(b);
	this.ux = (this.bx - this.ax) / distance;
	this.uy = (this.by - this.ay) / distance;
    }
    
    public Point currentLocation(){
    	Point p = new Point();
    	p.setLocation(this.x,this.y);
	return p;
    }

	/**
	* move forward this mobil by <code>step</code>.
	*/
    public void moveForward(){
    	this.x += this.step * this.ux;
    	this.y += this.step * this.uy;
    }

	/**
	* move backward this mobil by <code>step</code>.
	*/
    public void moveBackward(){
    	this.x -= this.step * this.ux;
    	this.y -= this.step * this.uy;
    }

	public void setStep(double step){
		this.step = step;
	}
	
	public double getStep(){
		return this.step;
	}
	
	
	/**
	* Return true if this mobil is betoween the segment end points.
	* A point M is located into the segment AB if the value of the
	* vector scalar product MA . MB is positif.
	*/ 
    public boolean isIntoBounds(){
	return ( (this.x - this.ax)*(this.x - this.bx) + (this.y - this.ay)*(this.y - this.by) ) <= 0;
    }
    
    public void reset(){
    	this.x = this.ax;
    	this.y = this.ay;
    }
}
