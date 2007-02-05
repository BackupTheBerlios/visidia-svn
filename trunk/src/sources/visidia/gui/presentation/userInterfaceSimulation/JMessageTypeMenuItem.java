package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
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
