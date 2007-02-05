package visidia.gui.presentation.userInterfaceSimulation;

import javax.swing.*;
import visidia.misc.*;

public class JMessageTypeMenuItem extends JCheckBoxMenuItem {
    protected MessageType messageType;

    public JMessageTypeMenuItem (MessageType messageType) {
	super (messageType.getType());
	setForeground(messageType.getColor());
	this.messageType = messageType;
    
    }
    public MessageType getMessageType(){
	return messageType;
    }
}
