package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.BorderLayout;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class HashTableFrame extends JFrame {

    

    public HashTableFrame(Hashtable<Object,Object> stats){
	
	HashTableModel tableModel;	
	JTable resultTable;
	JScrollPane scrollPane;
	tableModel = new HashTableModel(stats);
	resultTable = new JTable(tableModel);
	scrollPane = new JScrollPane(resultTable);		
	getContentPane().add(scrollPane,BorderLayout.CENTER);

	
    }
    
}


class HashTableModel extends AbstractTableModel {
    
    private Vector<Object> vKeysStats;
    private Vector<Object> vDataStats;
    
    public HashTableModel(Hashtable<Object,Object> hstats){
	hashTableToVectors(hstats);
    }
	
    public int getColumnCount(){
	return 2;
    }

    public int getRowCount(){
	return vKeysStats.size();
    }

    public Object getValueAt(int row, int col){
	switch (col){
	case 0: return vKeysStats.get(row);
	case 1: return vDataStats.get(row);
	}
	throw new IllegalArgumentException();
    }

    private void hashTableToVectors(Hashtable<Object,Object> hstats) {
	Set<Object> keys = hstats.keySet();

	vKeysStats = new Vector(hstats.size());
	vDataStats = new Vector(hstats.size());
	
	for(Object key : keys) {
	    vKeysStats.add(key);
	    vDataStats.add(hstats.get(key));
	}
    }

    public String getColumnName(int col) {
	switch (col){
	case 0: return "Keys";
	case 1: return "Values";
	}
	throw new IllegalArgumentException();
    }
}











