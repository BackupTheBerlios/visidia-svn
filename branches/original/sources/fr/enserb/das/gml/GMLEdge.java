package fr.enserb.das.gml;

public class GMLEdge {
	private Integer sourceId;
	private Integer targetId;
	
	public void setSourceId(Integer srcId){
		sourceId = srcId;
	}
	public Integer getSourceId(){
		return sourceId;
	}
	
	public void setTargetId(Integer targetId){
		this.targetId = targetId;
	}
	public Integer getTargetId(){
		return targetId;
	}
}