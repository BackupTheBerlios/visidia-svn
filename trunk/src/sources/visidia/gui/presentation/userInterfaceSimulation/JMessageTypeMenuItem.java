package visidia.gui.presentation.userInterfaceSimulation;

import javax.swing.*;
import visidia.misc.*;

public class JMessageTypeMenuItem extends JCheckBoxMenuItem {
    /**
	 * 
	 */
	private static final long serialVersionUID = 339131659120320997L;
	protected MessageType messageType;

    public JMessageTypeMenuItem (MessageType messageType) {
	super (messageType.getType());
	this.setForeground(messageType.getColor());
	this.messageType = messageType;
    
    }
    public MessageType getMessageType(){
	return this.messageType;
    }
}
