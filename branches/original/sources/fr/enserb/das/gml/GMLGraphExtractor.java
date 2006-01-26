package fr.enserb.das.gml;

import fr.enserb.das.graph.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * This class extracts graph from GMLPairSet.
 */
public class GMLGraphExtractor {
	static public SimpleGraph extractGraph(GMLList list){
		SimpleGraph graph = new SimpleGraph();
		GMLList graphElements = (GMLList) list.getValue("graph");
		if(graphElements != null){
			Enumeration enum = graphElements.getValues("node");
			while(enum.hasMoreElements()){
				GMLNode gmlNode = extractNode((GMLList)enum.nextElement());
				graph.put(gmlNode.getId());
				Vertex vertex = graph.vertex(gmlNode.getId());
				vertex.setData(gmlNode);
			}
			
			enum = graphElements.getValues("edge");
			while(enum.hasMoreElements()){
				GMLEdge gmlEdge = extractEdge((GMLList)enum.nextElement());
				graph.link(gmlEdge.getSourceId(),gmlEdge.getTargetId());
			}
		}
		return graph;
	}

	static public GMLNode extractNode(GMLList list){
		GMLNode gmlNode = new GMLNode();
		gmlNode.setId((Integer)list.getValue("id"));
		gmlNode.setLabel((String)list.getValue("label"));
		GMLList graphicList = (GMLList) list.getValue("graphics");
		if(graphicList != null){
			gmlNode.setGraphics(extractNodeGraphics(graphicList));
		}
		return gmlNode;
	} 

	static public GMLNodeGraphics extractNodeGraphics(GMLList list){
		GMLNodeGraphics graphics = new GMLNodeGraphics();
		Number number = null;
		int x = 0, y = 0, w = 10, h = 10;

		number = (Number) list.getValue("x");
		if(number != null){
			x = number.intValue();
		}
		number = (Number) list.getValue("y");
		if(number != null){
			y = number.intValue();
		}
		number = (Number) list.getValue("w");
		if(number != null){
			w = number.intValue();
		}
		number = (Number) list.getValue("h");
		if(number != null){
			h = number.intValue();
		}
		GMLList centerList = (GMLList) list.getValue("center");
		if(centerList != null){
			number = (Number) centerList.getValue("x");
			if(number != null){
				x = number.intValue() - w / 2;
			}
			number = (Number) centerList.getValue("y");
			if(number != null){
				y = number.intValue() - h / 2;
			}
		}
		graphics.setArea(new Area(new Rectangle(x,y,w,h)));

		graphics.setType((String) list.getValue("type"));
		graphics.setIcon((String) list.getValue("icon"));

		return graphics;
	}

	static public Properties extractNodeProperties(GMLList list){
		return null;
	}
	
	static public GMLEdge extractEdge(GMLList list){
		GMLEdge gmlEdge = new GMLEdge();
		gmlEdge.setSourceId((Integer)list.getValue("source"));
		gmlEdge.setTargetId((Integer)list.getValue("target"));
		return gmlEdge;
	}
}
