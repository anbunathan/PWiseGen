package edu.utep.pw.ga;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.utep.pw.ga.crossover.Crossover;
import edu.utep.pw.ga.fitness.Fitness;
import edu.utep.pw.ga.initialization.PopulationInitializer;
import edu.utep.pw.ga.mutation.Mutation;
import edu.utep.pw.ga.mutation.SmartMutation;
import edu.utep.pw.ga.replacement.Replacement;
import edu.utep.pw.ga.selection.Selection;
import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.ExpectedGenes;
import edu.utep.pw.ga.util.ParametersFileReader;
import edu.utep.pw.ga.util.RandomGenerator;
import edu.utep.pw.ga.util.ResultsFileWriter;

public class GeneticAlgorithm {

	private static final int     PRINT_EVERY_X_GEN     = Config.getPrintEveryX();
	
	private static final int     TEST_SET_SIZE_GOAL    = Config.getTestSetSize();
	private static final int     POPULATION_SIZE       = Config.getPopulationSize();
	private static final int     MAX_GENERATIONS       = Config.getMaxGenerations();
	private static final int     IMMIGRANT_EVERY_X_GEN = Config.getImmigrantEveryX();
	private static final int     NUM_REPRODUCTIONS     = Config.getNumberReproductions();

	private Fitness     fitnessFunction     = Config.getFitnessFunction();
	private Crossover   crossoverStrategy   = Config.getCrossoverStrategy();
	private Selection   parentSelector      = Config.getParentSelector();
	private Mutation    mutationStrategy    = Config.getMutationStrategy();
	private Replacement replacementStrategy = Config.getReplacementStrategy();
	
	private FitnessComparator fitnessComparator = new FitnessComparator();
	
	private GAInfo     gaInfo     = new GAInfo();
	private DomainInfo domainInfo = new DomainInfo();

	private ResultsFileWriter resultsWriter = new ResultsFileWriter();
	
	int[][] expectedgenes = null;
	
	public int[][] getExpectedgenes() {
		return expectedgenes;
	}

	public void setExpectedgenes(int[][] expectedgenes) {
		this.expectedgenes = expectedgenes;
	}

	public GeneticAlgorithm() {
		
		Config.getCrossoverStrategy().setExtraInfo(this.gaInfo, this.domainInfo);
		Config.getFitnessFunction().setExtraInfo(this.gaInfo, this.domainInfo);
		Config.getMutationStrategy().setExtraInfo(this.gaInfo, this.domainInfo);
		Config.getReplacementStrategy().setExtraInfo(this.gaInfo, this.domainInfo);
		Config.getParentSelector().setExtraInfo(this.gaInfo, this.domainInfo);
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
	
	private void initialize() {
		
		//Read the input file
		ParametersFileReader reader = new ParametersFileReader();
		reader.readFile();
		this.domainInfo.setParameters(reader.getParameters());
		this.domainInfo.setValues(reader.getValues());
		
		//Calculate total pairs
//		System.out.println("domainInfo.getValues().size() = "+this.domainInfo.getValues().size());
		int totalPairs = 0;
		for(int i = 0; i < this.domainInfo.getValues().size(); i++) {
			for(int j = i + 1; j < this.domainInfo.getValues().size(); j++) {
				
				Value value1 = this.domainInfo.getValues().get(i);
				Value value2 = this.domainInfo.getValues().get(j);
//				System.out.println("Value1 inside Initialize = "+value1.getId()+" "+value1.getName()+" "+value1.getOwner().getId());
//				System.out.println("Value2 inside Initialize = "+value2.getId()+" "+value2.getName()+" "+value2.getOwner().getId() );
				
				if(value1.getOwner().equals(value2.getOwner())) //Avoid pair values of the same parameter owner
					continue;

				totalPairs++;
			}
		}
		this.domainInfo.setTotalPairs(totalPairs);
		
		PopulationInitializer populationInitializer = Config.getPopulationInitializer();
		populationInitializer.setExtraInfo(gaInfo, domainInfo);
		Individual[] population = populationInitializer.createPopulation();
		
		gaInfo.setPopulation(population);
		
		this.gaInfo.setGeneration(0);
		this.gaInfo.setPopulationFitness(0);
	}
	
	private void execute() {

		//Calculate fitness of initial population
		for(Individual individual : this.gaInfo.getPopulation()) {
			
			int individualFitness = this.fitnessFunction.calculateFitness(individual);
			individual.setFitness(individualFitness);
			
			int individualPairs = this.calculatePairCount(individual);
			individual.setPairCount(individualPairs);

			int populationFitness = this.gaInfo.getPopulationFitness() + individualFitness;
			this.gaInfo.setPopulationFitness(populationFitness);
		}
		
		//Order initial population based on fitness
		Arrays.sort(this.gaInfo.getPopulation(), this.fitnessComparator);

		while(this.gaInfo.getGeneration() <= MAX_GENERATIONS && ! this.solutionFound()) {

			if(this.gaInfo.getGeneration() % PRINT_EVERY_X_GEN == 0) {
				System.out.println("Generation: "+ this.gaInfo.getGeneration() + " Best Fitness: "+ this.gaInfo.getPopulation()[0].getFitness() + ", Pairs: "+ this.gaInfo.getPopulation()[0].getPairCount());
				System.out.println("------------------");
				
				//this.resultsWriter.write(this.gaInfo.getGeneration() +","+ this.gaInfo.getPopulation()[0].getFitness() +","+ this.gaInfo.getPopulation()[0].getPairCount());
//				this.resultsWriter.write(this.gaInfo.getGeneration() +","+ this.gaInfo.getPopulation()[0].getFitness());
			}
			
			//Iterate depending on the number of reproductions in each generation
			for(int reproduction = 0; reproduction < NUM_REPRODUCTIONS; reproduction++) {
				
				//Select two parents for reproduction
				int[] parents = this.parentSelector.selectTwoParents();
				Individual parent1 = this.gaInfo.getPopulation()[parents[0]];
				Individual parent2 = this.gaInfo.getPopulation()[parents[1]];
				
				this.gaInfo.setLastTwoSelectedParentsRanks(parents);
				
				//Get the two children
				Individual[] offspring = this.crossoverStrategy.crossOver(parent1, parent2);
				
				//Mutate
//				this.mutationStrategy.mutate(offspring[0]);
//				this.mutationStrategy.mutate(offspring[1]);
				
				this.mutationStrategy.mutate(offspring[0]);
				this.mutationStrategy.mutate(offspring[1]);
				
				//Calculate fitness
				offspring[0].setFitness(this.fitnessFunction.calculateFitness(offspring[0]));
				offspring[1].setFitness(this.fitnessFunction.calculateFitness(offspring[1]));
				
				//Calculate pairs
				offspring[0].setPairCount(this.calculatePairCount(offspring[0]));
				offspring[1].setPairCount(this.calculatePairCount(offspring[1]));
				
				//Replacement
				this.replacementStrategy.replaceIndividuals(offspring);
			}

			//Immigrant ?
			if(this.gaInfo.getGeneration() % IMMIGRANT_EVERY_X_GEN == 0) {

				int index = RandomGenerator.getRandomInt((int)Math.ceil(POPULATION_SIZE / 2), POPULATION_SIZE - 1);
				
				Individual immigrant = new Individual(this.getRandomGenes());
				this.replacementStrategy.replaceSingleIndividual(index, immigrant);
			}

			if(this.gaInfo.getLastBestFitness() >= this.gaInfo.getPopulation()[0].getFitness())
				this.gaInfo.setNoImprovementCount(this.gaInfo.getNoImprovementCount() + 1);
			else
				this.gaInfo.setNoImprovementCount(0);
			
			
			this.gaInfo.setLastBestFitness(this.gaInfo.getPopulation()[0].getFitness());
			
			this.gaInfo.setGeneration(this.gaInfo.getGeneration() + 1);
		}
		
		if(this.gaInfo.getSolution() != null) {
//			System.out.println("SOLUTION FOUND!!! in generation "+ this.gaInfo.getGeneration());
//			this.resultsWriter.write("SOLUTION FOUND!!! in generation "+ this.gaInfo.getGeneration());
			
			this.printIndividual(this.gaInfo.getSolution());
		}
		else {
//			System.out.println("Max number of generations was reached, Individual with best fitness:");
//			this.resultsWriter.write("Max number of generations was reached, Individual with best fitness:");
			
			this.printIndividual(this.gaInfo.getPopulation()[0]);
		}
	}

	private int calculatePairCount(Individual individual) {
		
		int pairs = 0;

		int[] genes = individual.getGenes();
		boolean [][] pairsCaptured = new boolean[this.domainInfo.getValues().size()][this.domainInfo.getValues().size()];
		
		for(int testCase = 0; testCase < TEST_SET_SIZE_GOAL; testCase++) {

			int shift = this.domainInfo.getParameters().size() * testCase;
			
			for(int param1 = 0; param1 < this.domainInfo.getParameters().size(); param1++) {
				for(int param2 = param1 + 1; param2 < this.domainInfo.getParameters().size(); param2++) {
					
					int allele1 = genes[param1 + shift];
					int allele2 = genes[param2 + shift];
					
					if(pairsCaptured[allele1][allele2] == false) {
						pairsCaptured[allele1][allele2] = true;
						pairs++;
					}
				}
			}
		}
		return pairs;
	}
	
	private boolean solutionFound() {
		
		for(Individual individual : this.gaInfo.getPopulation()) {

			if(this.domainInfo.getTotalPairs() == individual.getPairCount()) {
				this.gaInfo.setSolution(individual);
				return true;
			}
		}
		return false;
	}
	
	private void printIndividual(Individual individual) {
		
		String strIndividual = "";
		String strIndividual1 = "";

		System.out.println();
//		this.resultsWriter.write("");
		
		strIndividual += "[ ("; 
		//System.out.print("[ (");
		for(int i = 0; i < individual.getGenes().length; i++) {

			if(i != 0 && i != individual.getGenes().length - 1 && i % this.domainInfo.getParameters().size() == 0)
				strIndividual += "),("; //System.out.print("),(");
			
			strIndividual += this.domainInfo.getValues().get(individual.getGenes()[i]).getName();
			//System.out.print(this.domainInfo.getValues().get(individual.getGenes()[i]).getName());
			
			if((i + 1) % this.domainInfo.getParameters().size() != 0)
				strIndividual += ","; //System.out.print(",");
		}
		strIndividual1 = strIndividual+") ]";
		strIndividual += ") ] Fitness: "+ individual.getFitness() +", Pairs: "+ individual.getPairCount();
		
		//System.out.print(") ] fitness: "+ individual.getFitness() +", Pairs"+ individual.getPairCount());
		
		System.out.println(strIndividual);
		this.resultsWriter.write(strIndividual1);
		
		System.out.println();
//		this.resultsWriter.write("");
	}
	
	public void start() {
	
		ThreadMXBean mx = ManagementFactory.getThreadMXBean();
		long start = mx.getCurrentThreadCpuTime();
		
		this.initialize();
//		System.out.println("Total pairs: "+ this.domainInfo.getTotalPairs());
//		System.out.println("------------------");
//		
//		this.resultsWriter.write("Total pairs: "+ this.domainInfo.getTotalPairs());
//		this.resultsWriter.write("------------------");
		
		this.execute();
		
		long end = mx.getCurrentThreadCpuTime();
//		System.out.println("Time -> "+ (((double)(end-start)/(double)1000000000)/(double)60) +" min");
//		System.out.println("Time -> "+ ((double)(end-start)/(double)1000000000) +" seg");
//
//		this.resultsWriter.write("Time -> "+ (((double)(end-start)/(double)1000000000)/(double)60) +" min");
//		this.resultsWriter.write("Time -> "+ ((double)(end-start)/(double)1000000000) +" seg");
		
		this.resultsWriter.close();
	}
	
	public static void main(String [] args) {
//		ExpectedGenes mExpectedGenes = new ExpectedGenes();
//		mExpectedGenes.ComputeExpectedGenes();
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		algorithm.start();
		Toolkit.getDefaultToolkit().beep();
		/*Scanner scan;
		HashMap<Integer,List<String>> TestIDTransitionsMap = new HashMap<>();
		int noofbranches=0;
		List<Integer> keys = new ArrayList<>();
		try {
			scan = new Scanner(new File("results.txt"));

			int linecounter = 0;

			while (scan.hasNext()) {
				String curLine = scan.nextLine();
				String[] splitted = curLine.split("\\),\\(");
//				System.out.println("length of splitted string"+ splitted.length);
				for(int testindex=0;testindex<splitted.length;testindex++)
				{
					Integer key = testindex+1;
					String testcase = splitted[testindex].trim();
					if(testcase.contains("[ "))
					{
						testcase = testcase.replaceAll("\\[ ", "");
					}
					if(testcase.contains(" ]"))
					{
						testcase = testcase.replaceAll(" ]", "");
					}
					if(testcase.contains("("))
					{
						testcase = testcase.replaceAll("\\(", "");
					}
					if(testcase.contains(")"))
					{
						testcase = testcase.replaceAll("\\)", "");
					}
//					System.out.println("testcase solution = "+testcase);
					String[] splittedtestcase = testcase.split(",");
//					System.out.println("length of splitted testcase" + splittedtestcase.length);
					noofbranches = splittedtestcase.length;
					List<String> value = new ArrayList<>();
					for(int branchindex=0;branchindex<noofbranches;branchindex++)
					{
						String transition = splittedtestcase[branchindex].trim();
						transition = transition.replaceAll("\"", "");
//						System.out.println("transition = "+transition);
						value.add(transition);
					}
					TestIDTransitionsMap.put(key, value);
				}				
				linecounter++;
			}
			System.out.println("TestIDTransitionsMap = " + TestIDTransitionsMap);
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}