package visidia.gui.donnees;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class PropertyTableModel extends AbstractPropertyTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2784104113451989507L;
	protected Hashtable defProps = null;
    
    
    /**
     * Constructs new empty property table model.
     */
    public PropertyTableModel(){
        this(null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public PropertyTableModel(Hashtable props){
        if(props == null){
            props = new Hashtable();
        }
        this.defProps = props;
        this.keys = new Vector(props.keySet());
    }

    public void setProperties(Hashtable props){
	this.defProps = props;
	this.keys = new Vector(props.keySet());
	this.fireTableDataChanged();
    }
    
    public void putProperty(String key, Object value){
	if (!this.keys.contains(key)) this.keys.add(key);
	this.defProps.put(key,value);
	this.fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = this.getValueAt(row,0);

        this.defProps.remove(key);
        this.keys.remove(row);

        this.fireTableDataChanged();
    }


    public Hashtable getProperties(){
        return (Hashtable) this.defProps.clone();
    }
    
    public Object getValueAt(int row, int col){
        switch(col){
        case 0: return this.keys.elementAt(row);
        case 1: return this.getTypeName(this.defProps.get(this.keys.elementAt(row)));
        case 2: return this.defProps.get(this.keys.elementAt(row));
        }
        throw new IllegalArgumentException();	
    }
    
    /**
     * Only value column cell are editable.
     */
    public boolean isCellEditable(int row, int col){
        
        Object obj = this.getValueAt(row,col);

        return ((col == 2) && (!this.keys.elementAt(row).equals("label"))
                               && (( obj instanceof String)
                                   || ( obj instanceof Integer)
                                   || ( obj instanceof Double)
                                   || ( obj instanceof Float)
                                   || ( obj instanceof Boolean)
                                   || ( obj instanceof Long)
                                   || ( obj instanceof Short)
                                   || ( obj instanceof Byte) 
                                   || ( obj instanceof Character)));
    }
    
    /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){
        if(!( row < this.defProps.size() ) && ( col == 2)){
            throw new IllegalArgumentException();
        }

        String value = (String) aValue;
	
        Object obj = this.getValueAt(row,col);
        
        try{
            
            if (obj instanceof String)
                this.defProps.put(this.keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                this.defProps.put(this.keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                this.defProps.put(this.keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                this.defProps.put(this.keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                this.defProps.put(this.keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                this.defProps.put(this.keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                this.defProps.put(this.keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                this.defProps.put(this.keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                this.defProps.put(this.keys.elementAt(row), Boolean.parseBoolean(value));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,
                                          e.getMessage(), 
                                          "Warning",
                                          JOptionPane.WARNING_MESSAGE); 
        }
        
        this.fireTableCellUpdated(row,col);

    }
}
