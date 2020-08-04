package edu.utep.pw.ga.fitness;

import java.util.Arrays;

import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.Parameter;
import edu.utep.pw.ga.Value;
import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.ExpectedGenes;

public class DifferentPairsFitness extends FitnessFunction {

	private static final int TEST_SET_SIZE_GOAL = Config.getTestSetSize();
	private static ExpectedGenes mExpectedGenes = new ExpectedGenes();
//	int[][] expectedgenes = {{0, 4, 8}, {1, 5, 8}, {2, 6, 8}, {3, 7, 8},{0, 5, 9}, {1, 4, 9},
//			{2, 7, 9}, {3, 6, 9}, {0, 6, 10}, {1, 7, 10},{2, 4, 10}, {3, 5, 10},
//			{0, 7, 11}, {1, 6, 11}, {2, 5, 11}, {3, 4, 11},{0, 4, 12}, {1, 5, 12},
//			{2, 6, 12}, {3, 7, 12}, {0, 5, 13}, {1, 4, 13},{2, 7, 13}, {3, 6, 13}};
	
//	int[][] expectedgenes = {{0, 3}, {0, 4}, {1, 3}, {1, 4}, {2, 3}, {2, 3}
//			};
//	
//	int[][] expectedgenes = {{0, 1}};
	int[][] expectedgenes = mExpectedGenes.ComputeExpectedGenes();;
	
/*	@Override
	public int calculateFitness(Individual individual) {
		
		int fitness = 0;

		int[] genes = individual.getGenes();
//		System.out.println("genes inside DifferentPairsFitness = "+Arrays.toString(genes));
		
		boolean [][] pairsCaptured = new boolean[this.domainInfo.getValues().size()][this.domainInfo.getValues().size()];
//		System.out.println("size inside DifferentPairsFitness = "+this.domainInfo.getValues().size());
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {

			int shift = this.domainInfo.getParameters().size() * testCase;
//			System.out.println("shift inside DifferentPairsFitness = "+shift);
//			System.out.println("size inside DifferentPairsFitness = "+this.domainInfo.getParameters().size());
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < this.domainInfo.getParameters().size(); param2++) {
//					System.out.println("param1+shift inside DifferentPairsFitness = "+(param1 + shift));
//					System.out.println("param2+shift inside DifferentPairsFitness = "+(param2 + shift));
					int allele1 = genes[param1 + shift];
					int allele2 = genes[param2 + shift];
//					System.out.println("allele1 inside DifferentPairsFitness = "+allele1);
//					System.out.println("allele2 inside DifferentPairsFitness = "+allele2);
					if(pairsCaptured[allele1][allele2] == false) {
						
						pairsCaptured[allele1][allele2] = true;
						fitness++;
					}
				}
			}
		}
//		System.out.println("fitness inside DifferentPairsFitness = "+fitness);
//		System.out.println("pairsCaptured inside DifferentPairsFitness = "+Arrays.deepToString(pairsCaptured));
		return fitness;
	}*/
	
	@Override
	public int calculateFitness(Individual individual) {
//		expectedgenes = mExpectedGenes.ComputeExpectedGenes();
		int fitness = 0;		
		int[] genes = individual.getGenes();
//		int[] expectedgenes = {0, 4, 8, 3, 6, 9, 1, 7, 10, 2, 5, 11, 2, 6, 12, 0, 5, 13};
		
//		System.out.println("genes inside DifferentPairsFitness = "+Arrays.toString(genes));
//		System.out.println("genes size inside DifferentPairsFitness = "+genes.length);
		/*for(int geneindex=0;geneindex<genes.length;geneindex++)
		{
			if(genes[geneindex]==expectedgenes[geneindex])
			{
				fitness++;
			}
		}*/
//		System.out.println("TEST_SET_SIZE_GOAL inside DifferentPairsFitness = "+TEST_SET_SIZE_GOAL);
//		System.out.println("parameter size inside DifferentPairsFitness = "+this.domainInfo.getParameters().size());
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++)
		{
			int shift = this.domainInfo.getParameters().size() * testCase;
			int[] allele = new int[this.domainInfo.getParameters().size()];
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++)
			{
				allele[param1] = genes[param1 + shift];
				
			}
			for(int expectedindex=0;expectedindex<expectedgenes.length;expectedindex++)
			{
				if(Arrays.equals(allele, expectedgenes[expectedindex]))
				{
					fitness++;
				}
			}
			
		}
		
		boolean [][] pairsCaptured = new boolean[this.domainInfo.getValues().size()][this.domainInfo.getValues().size()];
//		System.out.println("size inside DifferentPairsFitness = "+this.domainInfo.getValues().size());
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {

			int shift = this.domainInfo.getParameters().size() * testCase;
//			System.out.println("shift inside DifferentPairsFitness = "+shift);
//			System.out.println("size inside DifferentPairsFitness = "+this.domainInfo.getParameters().size());
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < this.domainInfo.getParameters().size(); param2++) {
//					System.out.println("param1+shift inside DifferentPairsFitness = "+(param1 + shift));
//					System.out.println("param2+shift inside DifferentPairsFitness = "+(param2 + shift));
					int allele1 = genes[param1 + shift];
					int allele2 = genes[param2 + shift];
//					System.out.println("allele1 inside DifferentPairsFitness = "+allele1);
//					System.out.println("allele2 inside DifferentPairsFitness = "+allele2);
					if(pairsCaptured[allele1][allele2] == false) {
						
						pairsCaptured[allele1][allele2] = true;
						fitness++;
						for(int tc = 0; tc < TEST_SET_SIZE_GOAL; tc++)
						{
							int shift1 = this.domainInfo.getParameters().size() * tc;
							int[] allele = new int[this.domainInfo.getParameters().size()];
							for(int paraminner = 0; paraminner < this.domainInfo.getParameters().size(); paraminner++)
							{
								allele[paraminner] = genes[paraminner + shift1];
								
							}
							for(int expectedindex=0;expectedindex<expectedgenes.length;expectedindex++)
							{
								if(Arrays.equals(allele, expectedgenes[expectedindex]))
								{
									pairsCaptured[allele1][allele2] = true;
									fitness++;
								}
							}
							
						}

					}
				}
			}
		} //End of FOR loop
		int [][] pairsCaptured1 = new int[this.domainInfo.getValues().size()][this.domainInfo.getValues().size()];
		int pairs = 0;
		int repeated = 0;
		int penalty=0;
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {

			int shift = this.domainInfo.getParameters().size() * testCase;
			
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < this.domainInfo.getParameters().size(); param2++) {
					
					int allele1 = genes[param1 + shift];
					int allele2 = genes[param2 + shift];
					
					if(pairsCaptured1[allele1][allele2] == 0) {
						pairs++;
					}
					else if(pairsCaptured1[allele1][allele2] > 1) {
						repeated++;
					}
					pairsCaptured1[allele1][allele2]++;
				}
			}
		}
		
		penalty = pairs - repeated;
		fitness = fitness+penalty;
//		System.out.println("fitness inside DifferentPairsFitness = "+fitness);
//		System.out.println("pairsCaptured inside DifferentPairsFitness = "+Arrays.deepToString(pairsCaptured));
		for(Parameter parameter : super.domainInfo.getParameters()) {
			for(Value value : parameter.getValidValues()) {
				boolean valuefound=false;
				for(int j = 0; j < individual.getGenes().length; j++) 
				{
					int gene = individual.getGenes()[j];
					if(value.getId()==gene)
					{
						valuefound=true;
						break;
					}					
					
				}
				if(valuefound==false)
				{
					fitness = fitness/2;
				}
								
			}
					
		}	
		
		
		return fitness;
	}
}