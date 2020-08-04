package edu.utep.pw.ga.util;

import java.io.File;
import java.io.FileWriter;

public class ResultsFileWriter {

    private static final String outputFileName = "results.txt";
    private FileWriter fw;
    
	
	public ResultsFileWriter() {
		
		try {
		
			String filePath = System.getProperty("user.dir") +  File.separator + outputFileName;
			File resultsFile = new File(filePath);
			
			if(resultsFile.isFile())
				resultsFile.delete();
		
			this.fw = new FileWriter(resultsFile, true);

		} catch (Exception ex) {
			throw new RuntimeException("Error while trying to create the ResultsFileWriter", ex);
		}
	}
	
	public void write(String line) {
		
		try {
			this.fw.write(line + "\n");			
		}
		catch (Exception ex) {
			
			try {
				this.fw.close();
			}
			catch(Exception e) {}
			
			throw new RuntimeException("Error while trying to write to "+ outputFileName, ex);
		}
	}
	
	public void close() {
		
		try {
			this.fw.close();
		}
		catch(Exception ex) {}
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
}
