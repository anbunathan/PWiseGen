package edu.utep.pw.ga.util;

import java.util.Random;

public class RandomGenerator {

	private static Random rand = new Random();
	
	public static int getRandomInt(int min, int max) {
		
		long range       = (long)max - (long)min + 1;
		long fraction    = (long)(range * rand.nextDouble());
	    int randomNumber = (int)(fraction + min);
	    
		return randomNumber;
	}
	
	public static double getRandomDouble() {
		return rand.nextDouble();
	}
}
