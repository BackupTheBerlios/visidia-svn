package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * This agent moves randomly in the graph.
 */
public class BasicAgent extends Agent {

    /**
     * This is the method every agent has to override in order to make
     * it work. When the agent  is started by the simulator, init() is
     * launched.
     */
    protected void init() {

        int i=0;
        char c='a';
        float f=0.0f;
        double d=0.0;
        byte by=0;
        long l=0;
        short sh=0;
        boolean bo=true;
        String st=new String("Coucou");
        Integer in= new Integer(0);

        /**
         * Uses an unpredictable deplacement. It chooses one door randomly.
         */
        setAgentMover("RandomAgentMover");



        do {
            
            setVertexProperty("int i",i++);
            setVertexProperty("char c",c++);
            setVertexProperty("float f",f++);
            setVertexProperty("double d",d++);
            setVertexProperty("byte by",by++);
            setVertexProperty("long l",l++);
            setVertexProperty("short sh",sh++);
            setVertexProperty("String st",st);

            try{
                in = (Integer)(getVertexProperty("Integer in")) + 1;
            }
            catch(Exception e) {
                //   setVertexProperty("Integer in",in);
            }

            setVertexProperty("Integer in",in);

            move();
        } while (true);
    }
}
