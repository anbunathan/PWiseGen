package edu.utep.pw.ga.replacement;

import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.util.Config;

public class ParentsComplementReplacement extends ReplacementStrategy {

	private static final int POPULATION_SIZE = Config.getPopulationSize();
	
	@Override
	public void replaceIndividuals(Individual[] offspring) {
		
		//Select two for removal
		int[] removals = new int[2];
		removals[0] = POPULATION_SIZE - super.gaInfo.getLastTwoSelectedParentRanks()[0] - 1;
		removals[1] = POPULATION_SIZE - super.gaInfo.getLastTwoSelectedParentRanks()[1] - 1;
		
		//Best individual elitism
		if(removals[0] == 0)
			removals[0] = removals[1] != 1 ? 1 : 2;
		if(removals[1] == 0)
			removals[1] = removals[0] != 1 ? 1 : 2;
		
		if(removals[1] > removals[0]) {
			int temp    = removals[0];
			removals[0] = removals[1];
			removals[1] = temp;
		}
		
		super.replaceSingleIndividual(removals[0], offspring[0]);
		super.replaceSingleIndividual(removals[1], offspring[1]);
	}
}
