package fr.enserb.das.misc;


/**
 * Random synchronis�.Permet d'obtenir des valeurs uniques 
 *dans un contexte distribu� et d'appels concurrents.
 */

import java.util.Random;

public class SynchronizedRandom  {

    private static Random generator =new Random();
    public static synchronized int nextInt(){
	return generator.nextInt();

    }

    public static synchronized float nextFloat(){
        return  generator.nextFloat();
    }

}
