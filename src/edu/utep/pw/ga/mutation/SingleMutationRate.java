package edu.utep.pw.ga.mutation;

import edu.utep.pw.ga.util.Config;
import edu.utep.pw.ga.util.RandomGenerator;

public class SingleMutationRate extends MutationStrategy {

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

}