package edu.utep.pw.ga.replacement;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public interface Replacement {

	void replaceIndividuals(Individual[] offspring);
	void replaceSingleIndividual(int removeIndex, Individual newIndividual);
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
