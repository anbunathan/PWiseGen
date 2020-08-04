package edu.utep.pw.ga.crossover;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public interface Crossover {

	Individual[] crossOver(Individual individual1, Individual individual2);
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
