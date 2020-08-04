package edu.utep.pw.ga.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ExpectedGenes {
	int[][] expectedgenes = null;
	public int[][] ComputeExpectedGenes()
	{
		Scanner scan;
		HashMap<String,List<String>> BranchTransitionsMap = new HashMap<>();
		HashMap<Integer,List<String>> TestIDTransitionsMap = new HashMap<>();
		int noofbranches=0;
		List<String> keys = new ArrayList<>();
		HashMap<String,Integer> EncodingHash = new HashMap<>();
		try {
			scan = new Scanner(new File("TestInput.txt"));

			int linecounter = 0;

			while (scan.hasNext()) {
				String curLine = scan.nextLine();
				String[] splitted = curLine.split("\t");
//				System.out.println("length of splitted string"
//						+ splitted.length);
				noofbranches = splitted.length;
				if (linecounter == 0) {
					for (int branchcount = 0; branchcount < noofbranches; branchcount++) {
						String key = splitted[branchcount].trim();
						List<String> value = new ArrayList<>();
						keys.add(key);
						BranchTransitionsMap.put(key, value);
					}
				} else {
					for (int branchcount = 0; branchcount < noofbranches; branchcount++) {
						String key = keys.get(branchcount);
						List<String> value = new ArrayList<>();
						if (BranchTransitionsMap.get(key) != null) {
							value = BranchTransitionsMap.get(key);

							if (!splitted[branchcount].trim().equals("")) {
//								System.out
//										.println("splitted[branchcount].trim() = "
//												+ splitted[branchcount].trim());
								value.add(splitted[branchcount].trim());
							}
							BranchTransitionsMap.put(key, value);
						} else {
							if (!splitted[branchcount].trim().equals("")) {
//								System.out
//										.println("splitted[branchcount].trim() = "
//												+ splitted[branchcount].trim());
								value.add(splitted[branchcount].trim());
							}
							BranchTransitionsMap.put(key, value);
						}
					}
				}
				linecounter++;
			}
//			System.out
//					.println("BranchTransitionsMap = " + BranchTransitionsMap);
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int encode=0;
		for(int keycounter=0;keycounter<keys.size();keycounter++)
		{
			String key = keys.get(keycounter);
//			System.out.println("key inside genetic algorithm = "+key);
			List<String> transitions = new ArrayList<>();
			transitions = BranchTransitionsMap.get(key);
			for(int branchcounter=0;branchcounter<transitions.size();branchcounter++)
			{
				String transition = transitions.get(branchcounter);
				EncodingHash.put(transition, encode++);
			}
		}
//		System.out.println("Encode hash = "+EncodingHash);
		int[][] expectedgenes=null;
		
		try {
			scan = new Scanner(new File("TestCaseOutput.txt"));
			
			int linecounter = 0;
			boolean headerstarted=false;
			boolean testcasestarted=false;
			List<Integer> testcasekeys = new ArrayList<>();
			while (scan.hasNext()) {
				String curLine = scan.nextLine();
//				System.out.println("curLine = "+curLine);
				if(curLine.equals(""))
				{
					testcasestarted=false;
				}
				if(testcasestarted==true)
				{
					String[] splitted = curLine.split("\t");				
//					System.out.println("length of splitted string"
//							+ splitted.length);
					String keystring = splitted[0].trim();
					Integer key = Integer.parseInt(keystring);
					testcasekeys.add(key);
					List<String> value = new ArrayList<>();
					for(int testcaseindex=1;testcaseindex<=noofbranches;testcaseindex++)
					{
						value.add(splitted[testcaseindex].trim());
					}
					TestIDTransitionsMap.put(key, value);
				}
				if(curLine.contains("case") && headerstarted==false)
				{
					headerstarted=true;
					String[] splitted = curLine.split("\t");				
//					System.out.println("length of splitted string"
//							+ splitted.length);
					noofbranches = splitted.length-2;
//					System.out.println("noofbranches = "+noofbranches);
				}
				if(curLine.contains("pairings") && testcasestarted==false)
				{
					testcasestarted=true;
				}
				linecounter++;
			}
			System.out.println("TestIDTransitionsMap = " + TestIDTransitionsMap);
//			System.out.println("testcasekeys = "+testcasekeys);
			expectedgenes = new int[testcasekeys.size()][noofbranches];
			for(int keyindex=0;keyindex<testcasekeys.size();keyindex++)
			{
				Integer key = testcasekeys.get(keyindex);
				List<String> testcaserow = new ArrayList<>();
				testcaserow = TestIDTransitionsMap.get(key);
				for(int testcaseindex=0;testcaseindex<testcaserow.size();testcaseindex++)
				{
					expectedgenes[keyindex][testcaseindex]=EncodingHash.get(testcaserow.get(testcaseindex));
				}
			}
			/*for(int i=0;i<testcasekeys.size();i++)
			{
				for(int j=0;j<noofbranches;j++)
				{
					System.out.println(expectedgenes[i][j]+" ");
				}
				System.out.println("");
			}*/
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expectedgenes;
	}
}
