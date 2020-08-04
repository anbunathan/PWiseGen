package edu.utep.pw.ga.replacement;

import java.util.Arrays;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.FitnessComparator;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public abstract class ReplacementStrategy implements Replacement {
	
	protected GAInfo     gaInfo;
	protected DomainInfo domainInfo;
		
	private final void insertIndividual(Individual individual) {
		
		int index = Arrays.binarySearch(this.gaInfo.getPopulation(), individual, new FitnessComparator());
		if(index < 0) {
			int insertIndex = -index-1;
			if(insertIndex == this.gaInfo.getPopulation().length)
				insertIndex--;
			System.arraycopy(this.gaInfo.getPopulation(), insertIndex, this.gaInfo.getPopulation(), insertIndex + 1, this.gaInfo.getPopulation().length-1-insertIndex);
			this.gaInfo.getPopulation()[insertIndex] = individual;
		}
		else {
			if(index == this.gaInfo.getPopulation().length)
				index--;
			System.arraycopy(this.gaInfo.getPopulation(), index, this.gaInfo.getPopulation(), index + 1, this.gaInfo.getPopulation().length-1-index);
			this.gaInfo.getPopulation()[index] = individual;
		}
	}
	
	private final void removeIndividual(int index) {
		System.arraycopy(this.gaInfo.getPopulation(), index + 1, this.gaInfo.getPopulation(), index, this.gaInfo.getPopulation().length-1-index);
	}
	
	public final void replaceSingleIndividual(int removeIndex, Individual newIndividual) {
		
		int populationFitness = this.gaInfo.getPopulationFitness();
		
		populationFitness -= this.gaInfo.getPopulation()[removeIndex].getFitness();
		this.removeIndividual(removeIndex);
		
		populationFitness += newIndividual.getFitness();
		this.insertIndividual(newIndividual);
		
		this.gaInfo.setPopulationFitness(populationFitness);
	}
	
	public final void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo) {
		this.gaInfo     = gaInfo;
		this.domainInfo = domainInfo;
	}
}