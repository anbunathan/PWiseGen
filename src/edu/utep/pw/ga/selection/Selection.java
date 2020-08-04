package edu.utep.pw.ga.selection;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;

public interface Selection {

	int[] selectTwoParents();
	void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo);
}
