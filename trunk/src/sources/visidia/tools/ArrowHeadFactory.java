package visidia.tools;

import java.awt.*;
import java.awt.geom.*;

public class ArrowHeadFactory {
    public static Shape createSegmentArrowHead(Point a, Point b, int width, int height){
	// orthogonal vector to segment AB
	double segmentLentgh = Math.sqrt(Math.pow((a.getX() - b.getX()),2)
					 + Math.pow((a.getY() - b.getY()),2));

	double vX = (a.getY() - b.getY()) / segmentLentgh;
	double vY = (a.getX() - b.getX()) / segmentLentgh;

	double uX = (b.getX() - a.getX()) / segmentLentgh;
	double uY = (a.getY() - b.getY()) / segmentLentgh;

	double xPoints[] = new double[3];
	double yPoints[] = new double[3];

	xPoints[0] = b.getX() + (width/2)*vX - height*uX;
	yPoints[0] = b.getY() + (width/2)*vY - height*uY;

	xPoints[1] = b.getX() - (width/2)*vX - height*uX;
	yPoints[1] = b.getY() - (width/2)*vY - height*uY;

	xPoints[2] = b.getX();
	yPoints[2] = b.getY();

	GeneralPath polygon = new GeneralPath();
	polygon.moveTo((float)xPoints[2], (float)yPoints[2]);

	for(int i = 0; i < xPoints.length; i++){
	    polygon.lineTo((float) xPoints[i], (float) yPoints[i]);
	}
	
	polygon.closePath();
	

	return polygon;
    }
}
