/**
 *sert a envoyer un message contenant plusieurs informations.
 **/
package visidia.misc;
import java.util.Vector;

public class VectorMessage extends Message {
    private Vector data;

    public VectorMessage(Vector v){
	data = v;
    }

    public VectorMessage(Vector v, MessageType type){
	setType (type);
	data = v;
    }

    public Vector getData(){
	return (Vector)data.clone();
    }

    public Object clone(){
	return new VectorMessage((Vector)data.clone(), getType());
    }

    public String toString(){
	/* Ancien code
	if(data.elementAt(0) instanceof Boolean)
	    return "<FIN>";

	String id = ((Integer)data.elementAt(0)).toString();
	String num =((Integer)data.elementAt(1)).toString();
    
	return "<" + id + ">";
	*/
	String result = "<";
	int size = data.size();
	if(size ==1) {
	    result+=data.elementAt(0)+">";
	} else {
	    result+=data.elementAt(0);
	    for(int i = 1; i<data.size(); i++) {
		result+=";"+(data.elementAt(i)).toString();
	    }
	    result += ">";
	}
	return result;
    }
}
