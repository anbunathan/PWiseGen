package edu.utep.pw.ga.initialization;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public interface PopulationInitializer {

	Individual[] createPopulation();
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
