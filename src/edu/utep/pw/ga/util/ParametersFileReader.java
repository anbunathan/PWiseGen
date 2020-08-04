package edu.utep.pw.ga.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import edu.utep.pw.ga.Parameter;
import edu.utep.pw.ga.Value;

public class ParametersFileReader {

    private static final String inputFileName = "input.txt";
    private File parametersFile;
    
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private ArrayList<Value>     values     = new ArrayList<Value>();

	private char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private String[] branch1 = {"ad","ae","dj","ej"};
	private String[] branch2 = {"bf","bg","fk","gk"};
	private String[] branch3 = {"ch","ci","hi","ih","hl","il"};
	private int first, second, third;
	
	public ParametersFileReader() {
		String filePath = System.getProperty("user.dir") +  File.separator + inputFileName;
		this.parametersFile = new File(filePath);
		
    	if(! this.parametersFile.exists())
    		throw new RuntimeException("Parameters File ("+ inputFileName +") does not exist, it should be placed at "+ System.getProperty("user.dir"));
	}
	
	public void readFile() {

		if(Config.isParamsFile())
			this.readParamsFile();
		else
			this.readCountsFile();
	}
	private void readParamsFile() {
		
		File file = this.parametersFile;
		
		try {
			
			Scanner fileScanner = new Scanner(file);
			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();
				
				if(line.length() == 0)
					continue;
								
				Scanner lineScanner = new Scanner(line);
				lineScanner.useDelimiter("\\s*\\:\\s*");
				
				Parameter parameter = new Parameter(lineScanner.next().trim());
				
				String validValues = lineScanner.next().trim();
				
				Scanner validValuesScanner = new Scanner(validValues);
				validValuesScanner.useDelimiter("\\s*\\,\\s*");

				while(validValuesScanner.hasNext()) {
					String valueName = validValuesScanner.next().trim();
					
					if(valueName.length() == 0)
						continue;
					
					Value value = new Value(valueName, parameter);
					
					this.values.add(value);
					parameter.getValidValues().add(value);
				}
				
				this.parameters.add(parameter);
			}
			
			fileScanner.close();
			
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred during input file read, verify the format", ex);
		}
	}
	
	private void readCountsFile() {
		
		File file = this.parametersFile;

		try {

			Scanner fileScanner = new Scanner(file);
			
			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();
				
				if(line.length() == 0)
					continue;
								
				Scanner lineScanner = new Scanner(line);
				lineScanner.useDelimiter("\\s*\\:\\s*");
				
				int parameterCount = lineScanner.nextInt();
				int valueCount     = lineScanner.nextInt();
				System.out.println("parameterCount inside Parameterfilereader = "+parameterCount);
				System.out.println("valueCount inside Parameterfilereader = "+valueCount);
				
				for(int i = 0; i < parameterCount; i++) {
					
					Parameter parameter = new Parameter(this.getNextParameterName());
					System.out.println("parameter.getName() inside Parameterfilereader = "+parameter.getName());
					
					for(int j = 0; j < valueCount; j++) {
						
						Value value = new Value(parameter.getName() + j, parameter);
						
						this.values.add(value);
						parameter.getValidValues().add(value);
					}
					
					this.parameters.add(parameter);
				}
			}
			
			fileScanner.close();
			
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred during input file read, verify the format", ex);
		}
	}
	
	private String getNextParameterName() {
		
		String parameterName = "" + letters[first] + "" + letters[second] + "" + letters[third];
//		String parameterName = "" + branch1[first] + "" + branch2[second] + "" + branch3[third];
		
		third++;
		if(third >= letters.length) {
			System.out.println("third has to be reset inside Parameterfilereader");
			third = 0;
			second++;
			if(second >= letters.length) {
				System.out.println("third has to be reset inside Parameterfilereader");
				second = 0;
				first++;
				if(first >= letters.length) {
					System.out.println("third has to be reset inside Parameterfilereader");
					first = 0;
				}
			}
		}
		
		return parameterName;
	}
	
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	
	public ArrayList<Value> getValues() {
		return values;
	}
}
