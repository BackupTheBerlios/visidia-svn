package visidia.gui.donnees;

import java.util.Vector;

import javax.swing.JOptionPane;

import visidia.tools.agents.WhiteBoard;


/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class AgentPropertyTableModel extends AbstractPropertyTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -823784020090568952L;
	protected WhiteBoard wb = null;
    
    /**
     * Constructs new empty property table model.
     */
    public AgentPropertyTableModel(){
        this(null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public AgentPropertyTableModel(WhiteBoard whiteboard){
        
        this.wb = whiteboard;
        
        this.keys = new Vector(this.wb.keys());
    }

    public void putProperty(String key, Object value){
	if (!this.keys.contains(key)) this.keys.add(key);
	this.wb.setValue(key,value);
	this.fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = this.keys.elementAt(row);

        this.keys.remove(row);
        this.wb.removeValue(key);

        this.fireTableDataChanged();
    }


    public Object getValueAt(int row, int col){
        switch(col){
        case 0: return this.keys.elementAt(row);
        case 1: return this.getTypeName(this.wb.getValue(this.keys.elementAt(row)));
        case 2: return this.wb.getValue(this.keys.elementAt(row));
        }
        throw new IllegalArgumentException();	
    }
    

    /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){
        if(!( row < this.keys.size() ) && ( col == this.valueColumn)){
            throw new IllegalArgumentException();
        }

        String value = (String) aValue;
	
        Object obj = this.getValueAt(row,col);
        
        try{
            
            if (obj instanceof String)
                this.wb.setValue(this.keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                this.wb.setValue(this.keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                this.wb.setValue(this.keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                this.wb.setValue(this.keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                this.wb.setValue(this.keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                this.wb.setValue(this.keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                this.wb.setValue(this.keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                this.wb.setValue(this.keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                this.wb.setValue(this.keys.elementAt(row), Boolean.parseBoolean(value));
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
