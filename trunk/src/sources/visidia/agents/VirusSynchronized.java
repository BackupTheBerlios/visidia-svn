package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * Implements  a  virus  with an  agent.  When  a  virus arrives  on  a
 * non-infected vertex, it send clones to all neighbours.
 *
 * @see Virus 
 */

public class VirusSynchronized extends SynchronizedAgent {

    protected void init() {

        /**
         * The implentation of this method  is a bit strange since the
         * code is written in the catch clause. If getVertexProperty()
         * does not  raise any exception, the vertex  has already been
         * infected  by  a  virus  and  then nothing  else  should  be
         * done.  Otherwise, the  vertex is  not yet  infected  and we
         * should infect it and infect neighbours.
         */

        try {
            getVertexProperty("alreadyInfected");
        } catch (NoSuchElementException e) {
            setVertexProperty("alreadyInfected", this);

            System.out.println("Vertex " + getVertexIdentity()
                               + " has been infected by "
                               + getIdentity());

            for(int i = 0; i < getArity(); ++i){
		sleep(1000);
		nextPulse();
                cloneAndSend(i);
	    }
        }
    }
}
