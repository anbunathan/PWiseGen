package edu.utep.pw.ga.initialization;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.Parameter;
import edu.utep.pw.ga.Value;
import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.RandomGenerator;

public class RandomPopulationInitializer implements PopulationInitializer {

	private static final int POPULATION_SIZE    = Config.getPopulationSize();
	private static final int TEST_SET_SIZE_GOAL = Config.getTestSetSize();
	
	private DomainInfo domainInfo;
	
	@Override
	public Individual[] createPopulation() {
		
		Individual[] population = new Individual[POPULATION_SIZE];
		//Generate random population
		for(int individual = 0; individual < POPULATION_SIZE; individual++) {

			population[individual] = new Individual(this.getRandomGenes());
		}
		
		return population;
	}
	
	private int[] getRandomGenes() {
		
		int[] genes = new int[this.domainInfo.getParameters().size() * TEST_SET_SIZE_GOAL];
		
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {
			
			int shift = this.domainInfo.getParameters().size() * testCase;
			
			for(int param = 0; param < this.domainInfo.getParameters().size(); param++) {
				Parameter parameter = this.domainInfo.getParameters().get(param);
				
				int randomIndex = RandomGenerator.getRandomInt(0, parameter.getValidValues().size() - 1);
				
				Value allele = parameter.getValidValues().get(randomIndex);
				
				genes[param + shift] = allele.getId();
			}
		}
		
		return genes;
	}

	@Override
	public void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo) {

		this.domainInfo = domainInfo;
	}

}
