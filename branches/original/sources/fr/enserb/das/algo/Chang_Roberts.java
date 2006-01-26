package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.Random;
import java.util.Vector;

public class Chang_Roberts extends Algorithm {
    
    public void init(){
        int myNb=getId().intValue();
        String label=new String();
        int answer;
        boolean run=true;
        
        label="("+getId()+","+myNb+")";
        putProperty("label",label);
        /*if (getId().intValue()==0)
            sendTo(0,new IntegerMessage(new Integer(myNb)));*/
        
        do {
            sendTo(nextDoor(),new IntegerMessage(new Integer(myNb)));
            Message msg = receiveFrom(previousDoor());
            answer= ((IntegerMessage)msg).value();
            
            if (answer==getId().intValue()) {
                putProperty("label",new String("E"));
                run=false;
            }
            
            if (answer>myNb) {
                myNb=answer;
                label="("+getId()+","+myNb+")";
                putProperty("label",label);
            }
        }
        while (run);
        
    }
    
    
    public Object clone(){
        return new Chang_Roberts();
    }
}
