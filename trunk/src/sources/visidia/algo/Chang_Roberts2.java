package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.Random;
import java.util.Vector;

public class Chang_Roberts2 extends Algorithm {
    
    public void init(){
        int myNb=getId().intValue();
        String label=new String();
        Door door=new Door();
        int answer;
        boolean run=true;

        label="("+getId()+","+myNb+")";
        putProperty("label",label);
        if (getId().intValue()==0)
            sendTo(0,new IntegerMessage(new Integer(myNb)));

        do {
            Message msg = receive(door);
            answer= ((IntegerMessage)msg).value();

            if (answer==getId().intValue())
                putProperty("label",new String("E"));
            if (answer>myNb) {
                myNb=answer;
                label="("+getId()+","+myNb+")";
                putProperty("label",label);
            }
            int s=door.getNum();
            if (answer!=getId().intValue())
                sendTo((s+1)%2,new IntegerMessage(new Integer(myNb)));
            else
                run=false;
        }
        while (run);

    }
    
    
    public Object clone(){
        return new Chang_Roberts2();
    }
}
