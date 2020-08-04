package edu.utep.pw.ga;

import java.util.ArrayList;

public class Parameter {

	private static int parameterID = 0; //Used for id-generator "getNextParameterID()"
	
	private int         id   = 0;
	private String      name = "";
	private ArrayList<Value> validValues = new ArrayList<Value>();

	public Parameter(String name) {
		this.id   = Parameter.getNextParameterID();
		this.name = name;
	}
	
	private static synchronized int getNextParameterID() {
		return Parameter.parameterID++;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Value> getValidValues() {
		return this.validValues;
	}
}