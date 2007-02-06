package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;

public class Chang_Roberts extends Algorithm {
    
    public void init(){
        int myNb=this.getId().intValue();
        String label=new String();
        int answer;
        boolean run=true;
        
        label="("+this.getId()+","+myNb+")";
        this.putProperty("label",label);
        /*if (getId().intValue()==0)
            sendTo(0,new IntegerMessage(new Integer(myNb)));*/
        
        do {
            this.sendTo(this.nextDoor(),new IntegerMessage(new Integer(myNb)));
            Message msg = this.receiveFrom(this.previousDoor());
            answer= ((IntegerMessage)msg).value();
            
            if (answer==this.getId().intValue()) {
                this.putProperty("label",new String("E"));
                run=false;
            }
            
            if (answer>myNb) {
                myNb=answer;
                label="("+this.getId()+","+myNb+")";
                this.putProperty("label",label);
            }
        }
        while (run);
        
    }
    
    
    public Object clone(){
        return new Chang_Roberts();
    }
}
