package visidia.misc;

/**
 * générateur de nombre aléatoire. Permet d'obtenir des valeurs
 * uniques dans un contexte distribué et d'appels concurrents.  Cette
 * objet contien un objet Random (de java) qui est static : donc
 * partagé par tout les noeuds
 */

import java.util.Random;

public class SynchronizedRandom {

	private static Random generator = new Random();

	public static synchronized int nextInt() {
		return SynchronizedRandom.generator.nextInt();

	}

	public static synchronized float nextFloat() {
		return SynchronizedRandom.generator.nextFloat();
	}

}
