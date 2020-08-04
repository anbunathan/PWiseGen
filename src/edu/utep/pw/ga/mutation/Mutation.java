package edu.utep.pw.ga.mutation;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;

public interface Mutation {

	void mutate(Individual individual);
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
