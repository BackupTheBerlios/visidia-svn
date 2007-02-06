package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;

public class Chang_Roberts2 extends Algorithm {
    
    public void init(){
        int myNb=this.getId().intValue();
        String label=new String();
        Door door=new Door();
        int answer;
        boolean run=true;

        label="("+this.getId()+","+myNb+")";
        this.putProperty("label",label);
        if (this.getId().intValue()==0)
            this.sendTo(0,new IntegerMessage(new Integer(myNb)));

        do {
            Message msg = this.receive(door);
            answer= ((IntegerMessage)msg).value();

            if (answer==this.getId().intValue())
                this.putProperty("label",new String("E"));
            if (answer>myNb) {
                myNb=answer;
                label="("+this.getId()+","+myNb+")";
                this.putProperty("label",label);
            }
            int s=door.getNum();
            if (answer!=this.getId().intValue())
                this.sendTo((s+1)%2,new IntegerMessage(new Integer(myNb)));
            else
                run=false;
        }
        while (run);

    }
    
    
    public Object clone(){
        return new Chang_Roberts2();
    }
}
