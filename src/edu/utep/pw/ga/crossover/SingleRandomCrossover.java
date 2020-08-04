package edu.utep.pw.ga.crossover;

import edu.utep.pw.ga.util.RandomGenerator;

public class SingleRandomCrossover extends CrossoverStrategy {

	@Override
	public int[] getCrossoverPoints(int chromosomeSize) {
		return new int[] { RandomGenerator.getRandomInt(0, chromosomeSize) };
	}

	@Override
	public boolean getCrossoverFlag() {
		return true;
	}
}
