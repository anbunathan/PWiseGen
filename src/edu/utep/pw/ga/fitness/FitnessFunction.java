package edu.utep.pw.ga.fitness;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;

public abstract class FitnessFunction implements Fitness {

	protected GAInfo gaInfo;
	protected DomainInfo domainInfo;
	
	public final void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo) {
		this.gaInfo     = gaInfo;
		this.domainInfo = domainInfo;
	}
}
