package edu.utep.pw.ga.fitness;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public interface Fitness {

	int calculateFitness(Individual individual);
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
