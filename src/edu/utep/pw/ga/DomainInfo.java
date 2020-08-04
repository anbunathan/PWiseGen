package edu.utep.pw.ga;

import java.util.ArrayList;

public class DomainInfo {
	
	private ArrayList<Parameter> parameters;
	private ArrayList<Value>     values;
	private int                  totalPairs;

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public ArrayList<Value> getValues() {
		return values;
	}
	void setValues(ArrayList<Value> values) {
		this.values = values;
	}
	
	public int getTotalPairs() {
		return totalPairs;
	}
	void setTotalPairs(int totalPairs) {
		this.totalPairs = totalPairs;
	}
}
