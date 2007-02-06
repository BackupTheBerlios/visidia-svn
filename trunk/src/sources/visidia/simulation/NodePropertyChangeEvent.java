package visidia.simulation;


import java.io.Serializable;

/**
 * NodePropertyChangeEvent object is used to notify the change of node
 * property.  A property is a couple of key and its associated
 * value. If value is <code>null</code> it notify a property removing.
 */
public class NodePropertyChangeEvent implements SimulEvent, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8655624022076056400L;
	private long evtNum;
    private Integer id = null;
    private Object key;
    private Object value;

    /**
     * Construct new node property change event object. 
     */
    public NodePropertyChangeEvent(Long eventNumber, Integer nodeId, Object key, Object value){
	id = nodeId;
	evtNum = eventNumber.longValue();
	this.key = key;
	this.value = value;
    }
    
    /**
     * return the event number.
     */
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * return SimulConstants.NODE_PROPERTY_CHANGE as event type.
     */
    public int type(){
	return SimulConstants.NODE_PROPERTY_CHANGE;
    }

    /**
     * return the node id whose the property change.
     */
    public Integer nodeId(){
	return id;
    }
    
    /**
     * return the changing property key.
     */
    public Object getKey(){
	return key;
    }

    /**
     * return the changing property value.
     */
    public Object getValue(){
	return value;
    }
}
    
