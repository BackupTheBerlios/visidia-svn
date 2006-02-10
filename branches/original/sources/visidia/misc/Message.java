package visidia.misc;
import java.io.Serializable;


/**
 * classe representant les messages interchanges entre les 
 * noeuds du reseaux. Pour s'assurer que deux noeuds du reseaux 
 * n'ont pas des references sur le meme objet message, ce dernier
 * est duplique (clone) avant d'etre mis dans la fil d'attente de 
 * destinataire. 
 */
public abstract class Message implements Cloneable,Serializable {
    private int lamportClockStamp;

    /**
     * Each message has a type. The programmer may specify the type of the
     * messages he creates. By default the type is defaultMessageType.
     */
    private MessageType type = MessageType.defaultMessageType;
    private boolean visualization=true;
    /**
     * this method is used internally.
     */
    public void setLamportClockStamp(int value){
	lamportClockStamp = value;
    }

    /**
     * this method is used internally.
     */
    public int getLamportClockStamp(){
	return lamportClockStamp;
    }

    public void setType (MessageType type){
	this.type = type;
    }

    public MessageType getType (){
	return type;
    }

    public abstract Object clone();
    
    public abstract String toString();

   public boolean getVisualization(){
	return visualization;
   }

  public void setVisualization(boolean s){
	visualization=s;
  }
}

