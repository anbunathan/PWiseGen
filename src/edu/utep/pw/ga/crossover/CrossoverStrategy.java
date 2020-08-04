package edu.utep.pw.ga.crossover;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;


public abstract class CrossoverStrategy implements Crossover {

	protected GAInfo     gaInfo;
	protected DomainInfo domainInfo;
	
	protected abstract boolean getCrossoverFlag();
	protected abstract int[] getCrossoverPoints(int chromosomeSize);
	
	public Individual[] crossOver(Individual individual1, Individual individual2) {

		Individual offspring1 = individual1.clone();
		Individual offspring2 = individual2.clone();
		
		if(this.getCrossoverFlag() == false)
			return new Individual[] { offspring1, offspring2 };
		
		int[] crossoverPoints = this.getCrossoverPoints(individual1.getGenes().length);
		
		if(crossoverPoints == null)
			throw new RuntimeException("The overriden method 'getCrossoverPoints()' returned a null value");
		
		//Perform the cross over
		for(int i = 0; i < crossoverPoints.length; i++) {
			
			if(i % 2 == 1)
				continue;
			
			if((i + 1) != crossoverPoints.length) {
				
				System.arraycopy(individual1.getGenes(), crossoverPoints[i], offspring2.getGenes(), crossoverPoints[i], crossoverPoints[i + 1] - crossoverPoints[i]);
				System.arraycopy(individual2.getGenes(), crossoverPoints[i], offspring1.getGenes(), crossoverPoints[i], crossoverPoints[i + 1] - crossoverPoints[i]);
			}
			else {
				
				System.arraycopy(individual1.getGenes(), crossoverPoints[i], offspring2.getGenes(), crossoverPoints[i], individual1.getGenes().length - crossoverPoints[i]);
				System.arraycopy(individual2.getGenes(), crossoverPoints[i], offspring1.getGenes(), crossoverPoints[i], individual2.getGenes().length - crossoverPoints[i]);
			}
		}
		
		return new Individual[] { offspring1, offspring2 };
	}
	
	public final void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo) {
		this.gaInfo     = gaInfo;
		this.domainInfo = domainInfo;
	}
}