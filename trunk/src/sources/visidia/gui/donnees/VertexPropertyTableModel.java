package visidia.gui.donnees;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class VertexPropertyTableModel extends PropertyTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5806391413971676061L;
	protected Hashtable properties = null;
    protected Hashtable hashKeys = null;
    
    /**
     * Constructs new empty property table model.
     */
    public VertexPropertyTableModel(){
        this(null,null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public VertexPropertyTableModel(Hashtable props, Hashtable def){
        if(props == null){
            props = new Hashtable();
        }

        this.properties = props;
        this.defProps = def;

        this.hashKeys = (Hashtable) def.clone();
        this.hashKeys.putAll(props);

        this.keys = new Vector(this.hashKeys.keySet());        

    }

    public void setProperties(Hashtable props){
	this.properties = props;
	this.keys = new Vector(props.keySet());
	this.fireTableDataChanged();
    }
    
    public void putProperty(String key, Object value){
	if (!this.keys.contains(key)) this.keys.add(key);
	this.properties.put(key,value);
	this.fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = this.keys.elementAt(row);

        this.properties.remove(key);
        this.keys.remove(row);
        this.hashKeys.remove(key);

        this.fireTableDataChanged();
    }

    public Hashtable getProperties(){
        return (Hashtable) this.hashKeys.clone();
    }

    private Object getValueFromHashtables(Object key) {
        if (this.properties.containsKey(key))
            return this.properties.get(key);
        
        return this.defProps.get(key);
    }
    
    public Object getValueAt(int row, int col){
        Object key;

        key = this.keys.elementAt(row);
            
        switch(col){
        case 0: return this.keys.elementAt(row);
        case 1: return this.getTypeName(this.getValueFromHashtables(key));
        case 2: return this.getValueFromHashtables(key);
        }
        throw new IllegalArgumentException();	
    }
    
     /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){

        String value=(String) aValue;

        if(!( row < this.properties.size() + this.defProps.size() ) && ( col == this.valueColumn)){
            throw new IllegalArgumentException();
        }
	
        Object obj = this.getValueAt(row,col);


        try{
            
            if (obj instanceof String)
                this.properties.put(this.keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                this.properties.put(this.keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                this.properties.put(this.keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                this.properties.put(this.keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                this.properties.put(this.keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                this.properties.put(this.keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                this.properties.put(this.keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                this.properties.put(this.keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                this.properties.put(this.keys.elementAt(row), Boolean.parseBoolean(value));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,
                                          e.getMessage(), 
                                          "Warning",
                                          JOptionPane.WARNING_MESSAGE); 
        }
        
        this.fireTableCellUpdated(row,col);
    }


    public void updateKeys() {
        this.hashKeys.putAll(this.properties);
        
        this.keys = new Vector(this.hashKeys.keySet());        
    }

}
