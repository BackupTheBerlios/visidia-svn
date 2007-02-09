package visidia.gml;

import java.awt.Color;
import java.awt.geom.Area;

public class GMLNodeGraphics {
	public static final String RECTANGLE_TYPE = "rectangle";
	public static final String ELLIPTIC_TYPE = "oval";
	
	private Area area;
	private String type;
	private Color fill;
	private String iconFile;
	
	public void setArea(Area area){
		this.area = area;
	}
	public Area getArea(){
		return this.area;
	}
	
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
	
	public void setFill(Color fill){
		this.fill = fill;
	}
	public Color getFill(){
		return this.fill;
	}

	public void setIcon(String iconFile){
		this.iconFile = iconFile;
	}
	public String getIcon(){
		return this.iconFile;
	}
}
