package edu.utep.pw.ga;

public class Value {

	private static int valueID = 0; //Used for id-generator "getNextValueID()"
	
	private int       id    = 0;    //Id of the parameter value
	private String    name  = "";   //User-defined name
	private Parameter owner = null; //Owner parameter
	
	public Value(String name, Parameter owner) {
		this.id    = Value.getNextValueID();
		this.name  = name;
		this.owner = owner;
	}

	private static synchronized int getNextValueID() {
		return Value.valueID++;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Parameter getOwner() {
		return this.owner;
	}
}
