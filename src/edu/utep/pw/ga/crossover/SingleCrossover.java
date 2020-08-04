package edu.utep.pw.ga.crossover;

public class SingleCrossover extends CrossoverStrategy {

	private int crossoverPoint;
	
	@Override
	public int[] getCrossoverPoints(int chromosomeSize) {
		
		if(this.crossoverPoint != 0)
			return new int[] { this.crossoverPoint };
		
		this.crossoverPoint = (int) Math.floor((float) chromosomeSize / (float) 2);

		return new int[] { this.crossoverPoint };
	}

	@Override
	public boolean getCrossoverFlag() {
		return true;
	}
}
