package edu.utep.pw.ga.selection;

import java.util.Arrays;

import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.RandomGenerator;

public class RouletteWheelSelector extends ParentSelector {
	
	private static final int POPULATION_SIZE = Config.getPopulationSize();

	@Override
	public int[] selectTwoParents() {

		int[] selected = new int[2];
		
		int[] cumulativeFitness = new int[POPULATION_SIZE];
		
		cumulativeFitness[0] = super.gaInfo.getPopulation()[0].getFitness();
		for(int i = 1; i < POPULATION_SIZE; i++) {
			cumulativeFitness[i] = cumulativeFitness[i - 1] + super.gaInfo.getPopulation()[i].getFitness();
		}
		
		//Spin the wheel - Parent 1
		int rand = RandomGenerator.getRandomInt(0, super.gaInfo.getPopulationFitness());
		int index = Arrays.binarySearch(cumulativeFitness, rand);
		if(index < 0)
			index = (-index-1);
		
		selected[0] = index;
		
		do {
			
			//Spin the wheel - Parent 2
			rand = RandomGenerator.getRandomInt(0, super.gaInfo.getPopulationFitness());
			index = Arrays.binarySearch(cumulativeFitness, rand);
			if(index < 0)
				index = (-index-1);
			
			selected[1] = index;
			
		} while (selected[0] == selected[1]);
		
		return selected;
	}
	
}
