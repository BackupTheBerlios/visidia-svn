package visidia.tools;

import java.awt.*;
//import java.util.*;

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
    	
    	ax = a.getX(); ay = a.getY();
    	bx = b.getX(); by = b.getY();
    	
	x = ax; y = ay;
	
	double distance = a.distance(b);
	ux = (bx - ax) / distance;
	uy = (by - ay) / distance;
    }
    
    public Point currentLocation(){
    	Point p = new Point();
    	p.setLocation(x,y);
	return p;
    }

	/**
	* move forward this mobil by <code>step</code>.
	*/
    public void moveForward(){
    	x += step * ux;
    	y += step * uy;
    }

	/**
	* move backward this mobil by <code>step</code>.
	*/
    public void moveBackward(){
    	x -= step * ux;
    	y -= step * uy;
    }

	public void setStep(double step){
		this.step = step;
	}
	
	public double getStep(){
		return step;
	}
	
	
	/**
	* Return true if this mobil is betoween the segment end points.
	* A point M is located into the segment AB if the value of the
	* vector scalar product MA . MB is positif.
	*/ 
    public boolean isIntoBounds(){
	return ( (x - ax)*(x - bx) + (y - ay)*(y - by) ) <= 0;
    }
    
    public void reset(){
    	x = ax;
    	y = ay;
    }
}
