package visidia.misc;

import java.awt.Color;
import java.io.Serializable;
/**
 * This class represents the type of the messages.
 *
 * The programmer can create as many types as he wants for his messages,
 * and specify for each type if he thinks that the messages must be displayed
 * or not. By default the messages are displayed.
 *
 * For each type of messages the programmer can also specify the color he
 * prefers to use when the messages will be displayed. The default color is red.
 *
 * During the simulation of the algorithm, the user knows all the types used
 * by the algorithm and can choose to display them or not.
 */
public class MessageType implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2042628623618748495L;

	private static final Color defaultColor = Color.red;

    private String typeName;

    /* indicates if the messages must be displayed or not */
    private boolean toPaint;

    private Color color;

    public MessageType(String typeName, boolean toPaint, Color color){
	this.typeName = typeName;
	this.toPaint = toPaint;
	this.color = color;
    }

    public MessageType(String typeName, boolean toPaint){
	this(typeName, toPaint, defaultColor);
    }

    public MessageType(String typeName){
	this(typeName, true);
    }

    public String getType(){
	return typeName;
    }

    public void setToPaint(boolean bool){
	toPaint=bool;
    }

    public boolean getToPaint(){
	return toPaint;
    }

    public void setColor(Color color){
	this.color = color;
    }

    public Color getColor(){
	return color;
    }

    /* This type is used each time that the programmer of the algorithm does not
       specify the type of a message. By default, the messages whose type is
       defaultMessageType are displayed.
    */
    public static final MessageType defaultMessageType = new MessageType("default");
}
