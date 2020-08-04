package edu.utep.pw.ga.crossover;

import java.util.Arrays;

import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.RandomGenerator;

public class MultipleRandomCrossover extends CrossoverStrategy {

	private static final int NUM_CROSSOVER_POINTS;
	static {
		try {
			NUM_CROSSOVER_POINTS = Integer.parseInt(Config.getUserDefinedValue("NumberCrossoverPoints"));
		}
		catch (Exception ex) {
			throw new RuntimeException("NumberCrossoverPoints could not be determined, verify the configuration file", ex);
		}
	}
	
	@Override
	public int[] getCrossoverPoints(int chromosomeSize) {

		int[] crossoverPoints = new int[NUM_CROSSOVER_POINTS];
		
		for(int i = 0; i < NUM_CROSSOVER_POINTS; i++) {
			crossoverPoints[i] = RandomGenerator.getRandomInt(0, chromosomeSize);
		}
		Arrays.sort(crossoverPoints);

		return crossoverPoints;
	}
	
	@Override
	public boolean getCrossoverFlag() {
		return true;
	}
}
