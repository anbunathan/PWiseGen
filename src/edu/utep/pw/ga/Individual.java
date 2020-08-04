package edu.utep.pw.ga;

import java.util.Arrays;

public class Individual {

	private int [] genes;
	private int pairCount = -1;
	private int fitness   = -1;
	
	public Individual(int[] genes) {
		this.genes = genes;
	}
	
	public int[] getGenes() {
		return genes;
	}
		
	public int getPairCount() {
		return pairCount;
	}
	
	void setPairCount(int pairCount) {
		this.pairCount = pairCount;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	@Override
	public Individual clone() {
		
		int[] genes = Arrays.copyOf(this.genes, this.genes.length);
		Individual individual = new Individual(genes);
		return individual;
	}
}
