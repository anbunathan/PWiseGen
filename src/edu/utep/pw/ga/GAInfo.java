package edu.utep.pw.ga;

public class GAInfo {

	private Individual[] population;
	private int          generation;
	private int          populationFitness;
	private Individual   solution;
	private int          lastBestFitness;
	private int          noImprovementCount;
	private int[]        lastTwoSelectedParentRanks;
		
	public Individual[] getPopulation() {
		return population;
	}
	void setPopulation(Individual[] population) {
		this.population = population;
	}
	
	public int getGeneration() {
		return generation;
	}
	void setGeneration(int generation) {
		this.generation = generation;
	}
	
	public int getPopulationFitness() {
		return populationFitness;
	}
	public void setPopulationFitness(int populationFitness) {
		this.populationFitness = populationFitness;
	}
	
	public Individual getSolution() {
		return solution;
	}
	void setSolution(Individual solution) {
		this.solution = solution;
	}
	
	public int getLastBestFitness() {
		return lastBestFitness;
	}
	void setLastBestFitness(int lastBestFitness) {
		this.lastBestFitness = lastBestFitness;
	}
	
	public int getNoImprovementCount() {
		return noImprovementCount;
	}
	void setNoImprovementCount(int noImprovementCount) {
		this.noImprovementCount = noImprovementCount;
	}
	
	public int[] getLastTwoSelectedParentRanks() {
		return lastTwoSelectedParentRanks;
	}
	void setLastTwoSelectedParentsRanks(int[] lastTwoSelectedParentRanks) {
		this.lastTwoSelectedParentRanks = lastTwoSelectedParentRanks;
	}
}
