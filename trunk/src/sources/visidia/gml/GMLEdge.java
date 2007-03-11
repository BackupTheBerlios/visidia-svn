package visidia.gml;

public class GMLEdge {
	private Integer sourceId;

	private Integer targetId;

	public void setSourceId(Integer srcId) {
		this.sourceId = srcId;
	}

	public Integer getSourceId() {
		return this.sourceId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public Integer getTargetId() {
		return this.targetId;
	}
}
