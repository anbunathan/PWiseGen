package edu.utep.pw.ga.mutation;

import java.util.Arrays;

import edu.utep.pw.ga.GeneticAlgorithm;
import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.Parameter;
import edu.utep.pw.ga.Value;
import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.ExpectedGenes;
import edu.utep.pw.ga.util.RandomGenerator;

public class SmartMutation extends MutationStrategy {

	private static int TEST_SET_SIZE_GOAL = Config.getTestSetSize();
	
	private static ExpectedGenes mExpectedGenes = new ExpectedGenes();
	
	
	private enum SmartMutationType { PROBABILITY, AFTER_X, BEFORE_X, EVERY_X, NO_IMPROVEMENT_X }
	
	private static final boolean            USE_SIMILARITY_MUTATION;
	private static final SmartMutationType  SIMILARITY_MUTATION_TYPE;
	private static final double             SIMILARITY_MUTATION_VALUE;
	private static final int                THRESHOLD_SIMILARITY;
	private static final boolean            REPLACE_WITH_SMART_TEST_CASE;

	private static final boolean            USE_VALUE_OCCURRENCE_MUTATION;
	private static final SmartMutationType  VALUE_OCCURRENCE_MUTATION_TYPE;
	private static final double             VALUE_OCCURRENCE_MUTATION_VALUE;
	private static final boolean            BALANCE_VALUE_OCCURRENCES;
	
	private static final boolean            USE_PAIR_OCCURRENCE_MUTATION;
	private static final SmartMutationType  PAIR_OCCURRENCE_MUTATION_TYPE;
	private static final double             PAIR_OCCURRENCE_MUTATION_VALUE;
	private static final int                THRESHOLD_PAIR_OCCURRENCES;
	
//	int[][] expectedgenes = {{0, 4, 8}, {1, 5, 8}, {2, 6, 8}, {3, 7, 8},{0, 5, 9}, {1, 4, 9},
//			{2, 7, 9}, {3, 6, 9}, {0, 6, 10}, {1, 7, 10},{2, 4, 10}, {3, 5, 10},
//			{0, 7, 11}, {1, 6, 11}, {2, 5, 11}, {3, 4, 11},{0, 4, 12}, {1, 5, 12},
//			{2, 6, 12}, {3, 7, 12}, {0, 5, 13}, {1, 4, 13},{2, 7, 13}, {3, 6, 13}};
//	int[][] expectedgenes = {{0, 3}, {0, 4}, {1, 3}, {1, 4}, {2, 3}, {2, 3}
//	};

//	int[][] expectedgenes = {{0, 1}	};
	
	int[][] expectedgenes = mExpectedGenes.ComputeExpectedGenes();;
	
	static {
		try {
			USE_SIMILARITY_MUTATION      = Boolean.parseBoolean(Config.getUserDefinedValue("UseSimilarityMutation"));
			SIMILARITY_MUTATION_TYPE     = SmartMutationType.valueOf(Config.getUserDefinedValue("SimilarityMutationType"));
			SIMILARITY_MUTATION_VALUE    = Double.parseDouble(Config.getUserDefinedValue("SimilarityMutationValue"));
			THRESHOLD_SIMILARITY         = Integer.parseInt(Config.getUserDefinedValue("ThresholdSimilarity"));
			REPLACE_WITH_SMART_TEST_CASE = Boolean.parseBoolean(Config.getUserDefinedValue("ReplaceWithSmartTestCase"));
			
			USE_VALUE_OCCURRENCE_MUTATION   = Boolean.parseBoolean(Config.getUserDefinedValue("UseValueOccurrenceMutation"));
			VALUE_OCCURRENCE_MUTATION_TYPE  = SmartMutationType.valueOf(Config.getUserDefinedValue("ValueOccurrenceMutationType"));
			VALUE_OCCURRENCE_MUTATION_VALUE = Double.parseDouble(Config.getUserDefinedValue("ValueOccurrenceMutationValue"));
			BALANCE_VALUE_OCCURRENCES       = Boolean.parseBoolean(Config.getUserDefinedValue("BalanceValueOccurences"));
			
			USE_PAIR_OCCURRENCE_MUTATION   = Boolean.parseBoolean(Config.getUserDefinedValue("UsePairOccurrenceMutation"));
			PAIR_OCCURRENCE_MUTATION_TYPE  = SmartMutationType.valueOf(Config.getUserDefinedValue("PairOccurrenceMutationType"));
			PAIR_OCCURRENCE_MUTATION_VALUE = Double.parseDouble(Config.getUserDefinedValue("PairOccurrenceMutationValue"));
			THRESHOLD_PAIR_OCCURRENCES     = Integer.parseInt(Config.getUserDefinedValue("ThresholdPairOcurrences"));
		}
		catch(Exception ex) {
			throw new RuntimeException("Smart Mutation failed when trying to initialize its values, verify the configuration file", ex);
		}
	}

	
	@Override
	public boolean getMutationFlag() {
		
		if(Config.getMutationRate() == 0)
			return false;
		
		int rate = (int)(1.0 / Config.getMutationRate());
		if(rate == 0)
			rate = 1;
		int rand = RandomGenerator.getRandomInt(1, rate);

		return rand == rate;
	}
	
	@Override
	public void mutate(Individual individual) {

		super.mutate(individual);
		
		this.startSmartMutations(individual);
	}
	
	
	private void checkValueOccurrences(Individual individual) {

		int[] occurrences = new int[super.domainInfo.getValues().size()];
		
		for(int i = 0; i < individual.getGenes().length; i++) {
			occurrences[individual.getGenes()[i]]++;
		}
//		System.out.println("occurrences inside SmartMutation = "+Arrays.toString(occurrences));
//		System.out.println("Genes length inside SmartMutation = "+individual.getGenes().length);
		for(int iValue = 0; iValue < occurrences.length; iValue++) {

			if(occurrences[iValue] == 0) {
				
				Parameter parameter = super.domainInfo.getValues().get(iValue).getOwner();
				
				int valueWithMaxOcurrences = iValue;
				for(Value value : parameter.getValidValues()) {
					if(occurrences[value.getId()] > occurrences[valueWithMaxOcurrences])
						valueWithMaxOcurrences = value.getId();
				}
				
				for(int j = 0; j < individual.getGenes().length; j++) {
					if(individual.getGenes()[j] == valueWithMaxOcurrences) {

						individual.getGenes()[j] = iValue;
//						System.out.println("Mutated allele inside SmartMutation = "+iValue);
						occurrences[valueWithMaxOcurrences]--;
						occurrences[iValue]++;

						if(! BALANCE_VALUE_OCCURRENCES || occurrences[iValue] >= occurrences[valueWithMaxOcurrences])
							break;
					}
				}
			}
		}
	}
	
	private int[] getSmartTestCaseFromPWTable(Individual individual, int gene) {
		boolean stcrequired = false;
		int[] testCase = new int[super.domainInfo.getParameters().size()];
//		expectedgenes = mExpectedGenes.ComputeExpectedGenes();
		Parameter parametertobechecked=null;
		int smarttestcasevalue = 999;
		for(Parameter parameter : super.domainInfo.getParameters()) {
			for(Value value : parameter.getValidValues()) {
				if(value.getId()==gene)
				{
					parametertobechecked = parameter;
					break;
				}					
			}
			if(parametertobechecked!=null)
			{
				break;
			}			
		}			
		if(parametertobechecked!=null)
		{
			for(Value value : parametertobechecked.getValidValues())
			{
				boolean valuefound = false;
				for(int j = 0; j < individual.getGenes().length; j++) 
				{
					if(individual.getGenes()[j] == value.getId()) 
					{
						valuefound = true;
						break;
					}
				}
				if(valuefound==false)
				{
					stcrequired = true;
					smarttestcasevalue = value.getId();
					break;
				}
			}
		}
		boolean smarttestcasefound = false;
		if(stcrequired)
		{
			for(int expectedindex=0;expectedindex<expectedgenes.length;expectedindex++)
			{
				int[] tabletestcase = expectedgenes[expectedindex];
				
				for(int tablegene: tabletestcase){
		            if(tablegene == smarttestcasevalue){
		            	testCase = tabletestcase;
		            	smarttestcasefound = true;
		            	break;
		            }
		        }
				if(smarttestcasefound==true)
				{
					break;
				}

			}
		}
		return testCase;
	}
	
	private boolean checkSmartTestCaseRequired(Individual individual, int allele)
	{
		boolean stcrequired = false;
		Parameter parametertobechecked=null;
		for(Parameter parameter : super.domainInfo.getParameters()) {
			for(Value value : parameter.getValidValues()) {
				if(value.getId()==allele)
				{
					parametertobechecked = parameter;
					break;
				}					
			}
			if(parametertobechecked!=null)
			{
				break;
			}			
		}			
		if(parametertobechecked!=null)
		{
			for(Value value : parametertobechecked.getValidValues())
			{
				boolean valuefound = false;
				for(int j = 0; j < individual.getGenes().length; j++) 
				{
					if(individual.getGenes()[j] == value.getId()) 
					{
						valuefound = true;
						break;
					}
				}
				if(valuefound==false)
				{
					stcrequired = true;
					break;
				}
			}
		}
		return stcrequired;
	}

	private int[] getSmartTestCase(Individual individual) {
		
		int[] testCase = new int[super.domainInfo.getParameters().size()];
		
		int[] occurrences = new int[super.domainInfo.getValues().size()];
		
		for(int i = 0; i < individual.getGenes().length; i++) {
			occurrences[individual.getGenes()[i]]++;
		}
//		System.out.println("occurrences inside SmartMutation = "+Arrays.toString(occurrences));
		for(Parameter parameter : super.domainInfo.getParameters()) {
			
			int valueWithMinOccurrences = parameter.getValidValues().get(0).getId();
			for(Value value : parameter.getValidValues()) {
				if(occurrences[value.getId()] < occurrences[valueWithMinOccurrences])
					valueWithMinOccurrences = value.getId();
			}
			
			testCase[parameter.getId()] = valueWithMinOccurrences;
		}
//		System.out.println("smart testCase inside SmartMutation = "+Arrays.toString(testCase));
		return testCase;
	}
	
	private int[] getRandomTestCase() {
		
		int[] testCase = new int[super.domainInfo.getParameters().size()];
		
		for(int param = 0; param < super.domainInfo.getParameters().size(); param++) {
			Parameter parameter = super.domainInfo.getParameters().get(param);
			
			int randomIndex = RandomGenerator.getRandomInt(0, parameter.getValidValues().size() - 1);
			
			Value allele = parameter.getValidValues().get(randomIndex);
			
			testCase[param] = allele.getId();
		}
//		System.out.println("random testCase inside SmartMutation = "+Arrays.toString(testCase));
		return testCase;
	}
	
	private void replaceSimilarTestCases(Individual individual) {
		
		int threshold = (int) Math.floor((float)super.domainInfo.getParameters().size() * (float)THRESHOLD_SIMILARITY / (float)100);
		int[] smarttestCase = new int[super.domainInfo.getParameters().size()];
//		System.out.println("threshold testCase inside replaceSimilarTestCases of SmartMutation = "+threshold);
		for(int tcMain = 0; tcMain < TEST_SET_SIZE_GOAL; tcMain++) {
			
			int shiftMain = super.domainInfo.getParameters().size() * tcMain;

			for(int tcCompare = tcMain + 1; tcCompare < TEST_SET_SIZE_GOAL; tcCompare++) {
				
				int shiftCompare = super.domainInfo.getParameters().size() * tcCompare;
				
				int similarities = 0;
				for(int i = 0; i < super.domainInfo.getParameters().size(); i++) {

					if(individual.getGenes()[i + shiftMain] == individual.getGenes()[i + shiftCompare])
					{
						similarities++;
//						System.out.println("similar gene inside SmartMutation = "+individual.getGenes()[i + shiftMain]);
						int gene = individual.getGenes()[i + shiftMain];
						boolean stcrequired = checkSmartTestCaseRequired(individual, gene);
//						System.out.println("Smart test case required inside SmartMutation = "+stcrequired);
						if(stcrequired)
						{
							smarttestCase = getSmartTestCaseFromPWTable(individual, gene);
//							System.out.println("Smart test case inside SmartMutation = "+Arrays.toString(smarttestCase));
							System.arraycopy(smarttestCase, 0, individual.getGenes(), shiftCompare, super.domainInfo.getParameters().size());
						}
					}
				}
//				System.out.println("similarities inside replaceSimilarTestCases of SmartMutation = "+similarities);
				if(similarities > threshold) {

					if(REPLACE_WITH_SMART_TEST_CASE)
					{
//						System.out.println("smart test case for replacement inside SmartMutation = "+Arrays.toString(this.getSmartTestCase(individual)));
						System.arraycopy(this.getSmartTestCase(individual), 0, individual.getGenes(), shiftCompare, super.domainInfo.getParameters().size());
						
					}
					else
					{
//						System.out.println("smart test case for replacement inside SmartMutation = "+Arrays.toString(this.getRandomTestCase()));
						System.arraycopy(this.getRandomTestCase(), 0, individual.getGenes(), shiftCompare, super.domainInfo.getParameters().size());
					}
				}
			}
		}
	}
	
	private void checkPairOcurrences(Individual individual) {
		
		int[] genes = individual.getGenes();
		int[][] occurrences = new int[super.domainInfo.getValues().size()][super.domainInfo.getValues().size()];
		
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {
			
			int shift = super.domainInfo.getParameters().size() * testCase;
			
			for(int param1 = 0; param1 < super.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < super.domainInfo.getParameters().size(); param2++) {
					
					int value1 = genes[param1 + shift];
					int value2 = genes[param2 + shift];
					
					occurrences[value1][value2]++;
				}
			}
		}
		
		for(int i = 0; i < super.domainInfo.getValues().size(); i++) {
			for(int j = i + 1; j < super.domainInfo.getValues().size(); j++) {
				
				Value value1 = super.domainInfo.getValues().get(i);
				Value value2 = super.domainInfo.getValues().get(j);
				
				if(value1.getOwner().equals(value2.getOwner()))
					continue;

				if(occurrences[i][j] == 0) {

					for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {
						int shift = super.domainInfo.getParameters().size() * testCase;
						
						int value1Temp = genes[value1.getOwner().getId() + shift];
						int value2Temp = genes[value2.getOwner().getId() + shift];
						
						if(occurrences[value1Temp][value2Temp] > THRESHOLD_PAIR_OCCURRENCES) {
							System.out.println("pair added inside checkPairOcurrences = "+i+","+j);
							genes[value1.getOwner().getId() + shift] = i;
							genes[value2.getOwner().getId() + shift] = j;
							
							break;
						}
					}
				}
			}
		}
	}
	
	private void startSmartMutations(Individual individual) {
		
		if(USE_SIMILARITY_MUTATION) {
			
			boolean flag = false;
			
			if(SIMILARITY_MUTATION_TYPE == SmartMutationType.PROBABILITY && SIMILARITY_MUTATION_VALUE != 0) {
				int rate = (int)(1.0 / SIMILARITY_MUTATION_VALUE);
				int rand = RandomGenerator.getRandomInt(0, rate);
				
				flag = rand == rate;
			}
			else if(SIMILARITY_MUTATION_TYPE == SmartMutationType.AFTER_X) {
				flag = super.gaInfo.getGeneration() > SIMILARITY_MUTATION_VALUE;
			}
			else if(SIMILARITY_MUTATION_TYPE == SmartMutationType.BEFORE_X) {
				flag = super.gaInfo.getGeneration() < SIMILARITY_MUTATION_VALUE;
			}
			else if(SIMILARITY_MUTATION_TYPE == SmartMutationType.EVERY_X) {
				flag = super.gaInfo.getGeneration() % SIMILARITY_MUTATION_VALUE == 0;
			}
			else if(SIMILARITY_MUTATION_TYPE == SmartMutationType.NO_IMPROVEMENT_X) {
				flag = super.gaInfo.getNoImprovementCount() >= SIMILARITY_MUTATION_VALUE;
			}
			
			if(flag)
				this.replaceSimilarTestCases(individual);
		}
		
		if(USE_VALUE_OCCURRENCE_MUTATION) {
			
			boolean flag = false;

			if(VALUE_OCCURRENCE_MUTATION_TYPE == SmartMutationType.PROBABILITY && VALUE_OCCURRENCE_MUTATION_VALUE != 0) {
				int rate = (int)(1.0 / VALUE_OCCURRENCE_MUTATION_VALUE);
				int rand = RandomGenerator.getRandomInt(0, rate);
				
				flag = rand == rate;
			}
			else if(VALUE_OCCURRENCE_MUTATION_TYPE == SmartMutationType.AFTER_X) {
				flag = super.gaInfo.getGeneration() > VALUE_OCCURRENCE_MUTATION_VALUE;
			}
			else if(VALUE_OCCURRENCE_MUTATION_TYPE == SmartMutationType.BEFORE_X) {
				flag = super.gaInfo.getGeneration() < VALUE_OCCURRENCE_MUTATION_VALUE;
			}
			else if(VALUE_OCCURRENCE_MUTATION_TYPE == SmartMutationType.EVERY_X) {
				flag = super.gaInfo.getGeneration() % VALUE_OCCURRENCE_MUTATION_VALUE == 0;
			}
			else if(VALUE_OCCURRENCE_MUTATION_TYPE == SmartMutationType.NO_IMPROVEMENT_X) {
				flag = super.gaInfo.getNoImprovementCount() >= VALUE_OCCURRENCE_MUTATION_VALUE;
			}
			
			if(flag)
				this.checkValueOccurrences(individual);
		}
		
		if(USE_PAIR_OCCURRENCE_MUTATION) {
			
			boolean flag = false;

			if(PAIR_OCCURRENCE_MUTATION_TYPE == SmartMutationType.PROBABILITY && PAIR_OCCURRENCE_MUTATION_VALUE != 0) {
				int rate = (int)(1.0 / PAIR_OCCURRENCE_MUTATION_VALUE);
				int rand = RandomGenerator.getRandomInt(0, rate);
				
				flag = rand == rate;
			}
			else if(PAIR_OCCURRENCE_MUTATION_TYPE == SmartMutationType.AFTER_X) {
				flag = super.gaInfo.getGeneration() > PAIR_OCCURRENCE_MUTATION_VALUE;
			}
			else if(PAIR_OCCURRENCE_MUTATION_TYPE == SmartMutationType.BEFORE_X) {
				flag = super.gaInfo.getGeneration() < PAIR_OCCURRENCE_MUTATION_VALUE;
			}
			else if(PAIR_OCCURRENCE_MUTATION_TYPE == SmartMutationType.EVERY_X) {
				flag = super.gaInfo.getGeneration() % PAIR_OCCURRENCE_MUTATION_VALUE == 0;
			}
			else if(PAIR_OCCURRENCE_MUTATION_TYPE == SmartMutationType.NO_IMPROVEMENT_X) {
				flag = super.gaInfo.getNoImprovementCount() >= PAIR_OCCURRENCE_MUTATION_VALUE;
			}
			
			if(flag)
				this.checkPairOcurrences(individual);
		}
	}
}
