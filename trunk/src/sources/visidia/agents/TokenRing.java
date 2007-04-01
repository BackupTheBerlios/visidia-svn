package visidia.agents;

import visidia.simulation.agents.Agent;

/*
 * Ceci est un algorithme qui simule la circulation d'un jeton dans un anneau
 * unidirectionnel, dont la version de base est inventé par Djkstra en 1974 et
 * le code est donné dans un modèle purment théorique appelé le modèle à état.
 *
 * Dans l'anneau, il  y a une machine (un processus) distingué qu'on  va nomé
 * le top, qui va jouer le role du homebase de l'agent(celui qui genere un
 * agent).  Le whitebord de chaque process i  contient un boolean pour coder
 * son etat: St(i) .  L'agent detient dans sa mémoire deux booleans: St pour
 * coder son etat et Token pour coder la circulation du jeton. 
 */


public class TokenRing extends Agent
{
	private static String StTrue = "A";
	private static String StFalse = "N";
	
	/**
	 * This is the method every agent has to override in order to make it work.
	 * When the agent is started by the simulator, init() is launched.
	 */
	protected void init()
	{
		/* Initiate the agent WB */
		this.setAgentBase(); 
		
		while (true)
		{
			this.lockVertexProperties();
			
			/* R0: au demmarage ou  à chaque  visite de la machine top,
			 * l'agent  initialise son St=St(0) et Token=false.
			 */
			if (this.isAgentBase())
			{
				this.setAgentSt(this.getVertexSt());
				this.setAgentToken(false);

			}
			/* chaque visite d'un sommet i 'autre que le top,
			 * l'agent execute l'une des régles suivantes:
			 */
			
			/* R1: if  (St != St(i) and ! Token) alors
			 * St=St(i); Token=true;
			 * next=(i+1)Mod N;
			 */
			else if (this.getAgentSt() != this.getVertexSt() && ! this.getAgentToken())
			{
				this.setAgentSt(this.getVertexSt());
				this.setAgentToken(true);
			}
			/* R2: if  (St != St(i) and Token) alors
			 * St(i)=St;
			 * next=(i+1)Mod N;
			 */
			else if (this.getAgentSt() != this.getVertexSt() && this.getAgentToken())
			{
				this.setVertexSt(this.getAgentSt());
			}
			/* R3: if  (St == St(i)) alors
			 * next=(i+1)Mod N;
			 */
			
			this.unlockVertexProperties();
			try
			{
				this.moveToDoor(1 - this.entryDoor());
			}
			catch (java.lang.IllegalStateException e)
			{
				this.moveToDoor(0);
			}
		}
	}

	private Boolean isAgentBase()
	{
		try
		{
			return new Boolean(this.getVertexIdentity() == (Integer) this.getProperty(new String("Base")));
		}
		catch (java.util.NoSuchElementException e)
		{
			this.setAgentBase();
			return true;
		}
	}

	private void setAgentBase()
	{
		this.setProperty(new String("Base"), new Integer(this.getVertexIdentity()));
		this.setVertexSt(true);
	}

	private void setAgentSt(Boolean b)
	{
		this.setProperty(new String("St"), new Boolean(b));
	}

	private Boolean getAgentSt()
	{
		try
		{
			return (Boolean) this.getProperty(new String("St"));
		}
		catch (java.util.NoSuchElementException e)
		{
			return false;
		}
	}

	private void setAgentToken(Boolean b)
	{
		this.setProperty(new String("Token"), new Boolean(b));
	}

	private Boolean getAgentToken()
	{
		try
		{
			return (Boolean) this.getProperty(new String("Token"));
		}
		catch (java.util.NoSuchElementException e)
		{
			return false;
		}
	}

	private void setVertexSt(Boolean b)
	{
		if (b)
		{
			this.setVertexProperty(new String("label"), new String(TokenRing.StTrue));
		}
		else
		{
			this.setVertexProperty(new String("label"), new String(TokenRing.StFalse));
		}
		//this.setVertexProperty(new String("St"), new Boolean(b));
	}

	private Boolean getVertexSt()
	{
		try
		{
			//return (Boolean) this.getVertexProperty(new String("St"));
			return ((String) this.getVertexProperty(new String("label"))).equals(TokenRing.StTrue);
		}
		catch (java.util.NoSuchElementException e)
		{
			return false;
		}
	}
}
