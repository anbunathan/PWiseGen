package edu.utep.pw.ga.replacement;

import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.util.Config;

public class WeakerIndividualsReplacement extends ReplacementStrategy {

	private static final int POPULATION_SIZE = Config.getPopulationSize();
	
	@Override
	public void replaceIndividuals(Individual[] offspring) {
		
		//Select two for removal
		int[] removals = new int[2];
		removals[0] = POPULATION_SIZE - 1;
		removals[1] = POPULATION_SIZE - 2;
		
		super.replaceSingleIndividual(removals[0], offspring[0]);
		super.replaceSingleIndividual(removals[1], offspring[1]);
	}
}
