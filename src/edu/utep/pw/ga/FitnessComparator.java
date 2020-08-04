package edu.utep.pw.ga;

import java.util.Comparator;


public class FitnessComparator implements Comparator<Individual> {

	@Override
	public int compare(Individual i1, Individual i2) {
		Integer fitness1 = i1.getFitness();
		Integer fitness2 = i2.getFitness();
		return fitness2.compareTo(fitness1);
	}
}
