package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Implements  a virus  with  an agent.   When  a virus  arrives on  a
 * non-infected vertex, it sends clones to all neighbours.
 */

public class Virus extends Agent {

    protected void init() {

        /**
         * The implentation of this method  is a bit strange since the
         * code is written in the catch clause. If getVertexProperty()
         * does not  raise any exception, the vertex  has already been
         * infected  by  a  virus  and  then nothing  else  should  be
         * done.  Otherwise, the  vertex is  not yet  infected  and we
         * should infect it and infect neighbours.
         */

        this.lockVertexProperties();

        try {
            this.getVertexProperty("alreadyInfected");
            this.unlockVertexProperties();
        } catch (NoSuchElementException e) {
            this.setVertexProperty("alreadyInfected", this);
            this.setVertexProperty("label", "B");

            this.unlockVertexProperties();


            for(int i = 0; i < this.getArity(); ++i)
                this.cloneAndSend(i);
        }
    }
}
