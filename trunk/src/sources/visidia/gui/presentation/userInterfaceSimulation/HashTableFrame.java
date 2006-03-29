package visidia.gui.presentation.userInterfaceSimulation;

import visidia.tools.HashTableModel;

import java.awt.BorderLayout;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HashTableFrame extends JFrame {
    
    ReadOnlyHashTableModel tableModel;	

    public HashTableFrame(Map stats) {
	
	JTable resultTable;
	JScrollPane scrollPane;
	tableModel = new ReadOnlyHashTableModel(stats);
	resultTable = new JTable(tableModel);
	scrollPane = new JScrollPane(resultTable);		
	getContentPane().add(scrollPane,BorderLayout.CENTER);

	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public HashTableModel getTableModel() {
        return tableModel;
    }

}

class ReadOnlyHashTableModel extends HashTableModel {
    
    public ReadOnlyHashTableModel(Map table){
        super(table);
    }
}
