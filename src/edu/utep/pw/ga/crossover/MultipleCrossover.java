package edu.utep.pw.ga.crossover;

import edu.utep.pw.ga.util.Config;

public class MultipleCrossover extends CrossoverStrategy {

	private static final int NUM_CROSSOVER_POINTS;
	static {
		try {
			NUM_CROSSOVER_POINTS = Integer.parseInt(Config.getUserDefinedValue("NumberCrossoverPoints"));
		}
		catch (Exception ex) {
			throw new RuntimeException("NumberCrossoverPoints could not be determined, verify the configuration file", ex);
		}
	}
	
	private int[] crossoverPoints;

	@Override
	public int[] getCrossoverPoints(int chromosomeSize) {
		
		if(this.crossoverPoints != null)
			return crossoverPoints;
		
		this.crossoverPoints = new int[NUM_CROSSOVER_POINTS];
		
		int increments = (int) Math.floor((float) chromosomeSize / (float) (NUM_CROSSOVER_POINTS + 1));
		for(int i = 0; i < NUM_CROSSOVER_POINTS; i++) {
			this.crossoverPoints[i] = (i + 1) * increments;
		}

		return this.crossoverPoints;
	}
	
	@Override
	public boolean getCrossoverFlag() {
		return true;
	}
}
