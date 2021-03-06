package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import visidia.gui.metier.simulation.AlgoChoice;
import visidia.misc.MessageType;

//this class will allow us to choose which messages we want to
//visualize in the graphical interface.

public class ChoiceMessage2 extends JMenu implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7647469778187700625L;

	private Collection menusNames;

	public ChoiceMessage2(AlgoChoice algoChoice, Collection menusNames) {
		this.menusNames = menusNames;
		this.setListTypes(menusNames);

	}

	public ChoiceMessage2(AlgoChoice algoChoice) {
		super("Messages Type");
		if (algoChoice.getTableAlgo().isEmpty()) {
			// the list
			this.menusNames = new LinkedList();
		}
	}

	public void setListTypes(Collection lt) {
		this.removeAll();// in order to remove all the previous menu items
		// and set others

		this.menusNames = lt;
		Iterator it = lt.iterator();
		while (it.hasNext()) {
			MessageType messageType = (MessageType) it.next();
			JCheckBoxMenuItem checkBox = new JMessageTypeMenuItem(messageType);
			this.add(checkBox);
			checkBox.addActionListener(this);
			checkBox.setState(messageType.getToPaint());
		}
	}

	public void addAtListTypes(Collection cl) {
		this.removeAll();
		this.menusNames.addAll(cl);
		this.setListTypes(this.menusNames);
	}

	public void actionPerformed(ActionEvent evt) {
		JMessageTypeMenuItem checkBox = (JMessageTypeMenuItem) evt.getSource();
		// String ch=checkBox.getText();
		// System.out.println("vous avez clique sur la case"+ch);
		checkBox.getMessageType().setToPaint(checkBox.getState());
	}
}
