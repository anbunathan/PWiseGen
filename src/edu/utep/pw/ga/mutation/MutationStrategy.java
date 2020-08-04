package edu.utep.pw.ga.mutation;

import edu.utep.pw.ga.DomainInfo;
import edu.utep.pw.ga.GAInfo;
import edu.utep.pw.ga.Individual;
import edu.utep.pw.ga.Parameter;
import edu.utep.pw.ga.util.RandomGenerator;

public abstract class MutationStrategy implements Mutation {

	protected GAInfo     gaInfo;
	protected DomainInfo domainInfo;
	
	protected abstract boolean getMutationFlag();
	
	public void mutate(Individual individual) {
		
		if(this.getMutationFlag() == false)
			return;

		int geneIndex       = RandomGenerator.getRandomInt(0, individual.getGenes().length - 1); //Get the position of the gene that will be mutated
		Parameter parameter = this.domainInfo.getParameters().get(geneIndex % this.domainInfo.getParameters().size());
		int alleleIndex     = RandomGenerator.getRandomInt(0, parameter.getValidValues().size() - 1); //Get the position of the allele/value that will be chosen
		int allele          = parameter.getValidValues().get(alleleIndex).getId();
//		System.out.println("Mutated allele inside MutationStrategy = "+allele);
		individual.getGenes()[geneIndex] = allele;
	}
	
	public final void setExtraInfo(GAInfo gaInfo, DomainInfo domainInfo) {
		this.gaInfo     = gaInfo;
		this.domainInfo = domainInfo;
	}
}
