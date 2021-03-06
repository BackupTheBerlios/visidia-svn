package visidia.simulation;

import java.io.Serializable;

import visidia.misc.EdgeColor;

/**
 * This classe represents the event associating with the color change of an edge
 * 
 * @version 1.0
 */
public class EdgeColorChangeEvent implements SimulEvent, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3113232314871301380L;

	private long evtNum;

	private Integer id1 = null;

	private Integer id2 = null;

	private EdgeColor ec = null;

	/**
	 * Build an event associated to the color change of an edge
	 * 
	 * @param nodeId1
	 *            The first vertex of the edge
	 * @param nodeId2
	 *            The second vertex of the edge
	 * @param EdgeColor
	 *            The new color
	 */
	public EdgeColorChangeEvent(Long eventNumber, Integer nodeId1,
			Integer nodeId2, EdgeColor newColor) {
		this.ec = (EdgeColor) newColor.clone();
		this.id1 = new Integer(nodeId1.intValue());
		this.id2 = new Integer(nodeId2.intValue());
		this.evtNum = eventNumber.longValue();
	}

	/**
	 * Returns the number of the event
	 * 
	 * @return <code>Long</code>
	 */
	public Long eventNumber() {
		return new Long(this.evtNum);
	}

	/**
	 * Returns the type of the event
	 * 
	 * @return <code>SimulConstants.EDGE_COLOR_CHANGE</code> The number of the
	 *         event
	 */
	public int type() {
		return SimulConstants.EDGE_COLOR_CHANGE;
	}

	/**
	 * Returns the ID of the first node
	 * 
	 * @return <code>id1</code>
	 */
	public Integer nodeId1() {
		return this.id1;
	}

	/**
	 * Returns the ID of the second node
	 * 
	 * @return <code>id2</code>
	 */
	public Integer nodeId2() {
		return this.id2;
	}

	/**
	 * Return the new color of the edge
	 * 
	 * @return <code>ec</code> The new EdgeColor
	 */
	public EdgeColor color() {
		return this.ec;
	}
}
