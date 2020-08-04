package edu.utep.pw.ga.fitness;

import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.util.Config;

public class RepeatedPairsPenalizationFitness extends FitnessFunction {

	private static final int TEST_SET_SIZE_GOAL = Config.getTestSetSize();
	
	@Override
	public int calculateFitness(Individual individual) {
		
		int fitness = 0;
		
		int pairs = 0;
		int repeated = 0;

		int[] genes = individual.getGenes();
		int [][] pairsCaptured = new int[this.domainInfo.getValues().size()][this.domainInfo.getValues().size()];
		
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {

			int shift = this.domainInfo.getParameters().size() * testCase;
			
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < this.domainInfo.getParameters().size(); param2++) {
					
					int allele1 = genes[param1 + shift];
					int allele2 = genes[param2 + shift];
					
					if(pairsCaptured[allele1][allele2] == 0) {
						pairs++;
					}
					else if(pairsCaptured[allele1][allele2] > 2) {
						repeated++;
					}
					pairsCaptured[allele1][allele2]++;
				}
			}
		}
		
		fitness = pairs - repeated;
		
		return fitness;
	}
}