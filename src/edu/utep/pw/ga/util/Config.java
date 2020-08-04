package edu.utep.pw.ga.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import edu.utep.pw.ga.crossover.Crossover;
import edu.utep.pw.ga.fitness.Fitness;
import edu.utep.pw.ga.initialization.PopulationInitializer;
import edu.utep.pw.ga.mutation.Mutation;
import edu.utep.pw.ga.replacement.Replacement;
import edu.utep.pw.ga.selection.Selection;;

public class Config {
	
    private static Properties   properties              = new Properties();
    private static final String userDirectory           = "user.dir";
    private static final String defaultPropertyFileName = "ga-config.xml";
    
    static {
        try {
            String propertyFileName  = System.getProperty(userDirectory) +  File.separator + defaultPropertyFileName;
            FileInputStream propFile = new FileInputStream(propertyFileName);
            Config.properties.loadFromXML(propFile);
            
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration file.", e);
        }
    }
    
    //General GA configurations
    private static int          testSize             = 0;
    private static int          populationSize       = 0;
    private static int          maxGenerations       = 0;
    private static double       mutationRate         = 0.0;
    private static Fitness      fitnessFunction      = null;
    private static Crossover    crossoverStrategy    = null;
    private static Selection    parentSelector       = null;
    private static Replacement  replacementStrategy  = null;
    private static Mutation     mutationStrategy     = null;
    private static PopulationInitializer populationInitializer = null;
    
    public static boolean isParamsFile() {
    	
    	try {
    		return Boolean.parseBoolean(properties.getProperty("IsParamsFile"));
    	}
    	catch(Exception ex) {
    		throw new RuntimeException("IsParamsFile could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getPrintEveryX() {
    	
    	try {
    		return Integer.parseInt(properties.getProperty("PrintEveryX"));
    	}
    	catch(Exception ex) {
    		throw new RuntimeException("PrintEveryX could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getPopulationSize() {
    	
    	if(Config.populationSize != 0)
    		return Config.populationSize;
    	
    	String sizeConfig = properties.getProperty("PopulationSize");

    	try {
    		Config.populationSize = Integer.parseInt(sizeConfig);
    		return Config.populationSize; 
    	}
    	catch(NumberFormatException ex) {
    		throw new RuntimeException("PopulationSize could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getTestSetSize() {
    	
    	if(Config.testSize != 0)
    		return Config.testSize;
    	
    	String sizeConfig = properties.getProperty("TestSetSize");

    	try {
    		Config.testSize = Integer.parseInt(sizeConfig);
    		return Config.testSize;
    	}
    	catch(NumberFormatException ex) {
    		throw new RuntimeException("TestSetSize could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getMaxGenerations() {
    	
    	if(Config.maxGenerations != 0)
    		return Config.maxGenerations;
    	
    	String maxConfig = properties.getProperty("MaxGenerations");
    	
    	try {
    		Config.maxGenerations = Integer.parseInt(maxConfig);
    		return Config.maxGenerations;
    	}
    	catch(NumberFormatException ex) {
    		throw new RuntimeException("MaxGenerations could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static Fitness getFitnessFunction() {
    	
    	if(Config.fitnessFunction != null)
    		return Config.fitnessFunction;
    	
    	String function = Config.properties.getProperty("FitnessFunction");
    	
    	try {
    		Config.fitnessFunction = (Fitness)Class.forName(function).newInstance();
			return Config.fitnessFunction;
		} catch (Exception ex) {
			throw new RuntimeException("FitnessFunction could not be determined, verify the configuration file", ex);
		}
    }
    
    public static Crossover getCrossoverStrategy() {
    	
    	if(Config.crossoverStrategy != null)
    		return Config.crossoverStrategy;
    	
    	String strategy = Config.properties.getProperty("CrossoverStrategy");
    	
    	try {
    		Config.crossoverStrategy = (Crossover)Class.forName(strategy).newInstance();
			return Config.crossoverStrategy;
		} catch (Exception ex) {
			throw new RuntimeException("CrossoverStrategy could not be determined, verify the configuration file", ex);
		}
    }
        
    public static Selection getParentSelector() {
    	
    	if(Config.parentSelector != null)
    		return Config.parentSelector;
    	
    	String selector = Config.properties.getProperty("ParentSelector");
    	
    	try {
    		Config.parentSelector = (Selection)Class.forName(selector).newInstance();
			return Config.parentSelector;
		} catch (Exception ex) {
			throw new RuntimeException("ParentSelector could not be determined, verify the configuration file", ex);
		}
    }

    public static Replacement getReplacementStrategy() {
    	
    	if(Config.replacementStrategy != null)
    		return Config.replacementStrategy;
    	
    	String selector = Config.properties.getProperty("ReplacementSelector");
    	
    	try {
    		Config.replacementStrategy = (Replacement)Class.forName(selector).newInstance();
			return Config.replacementStrategy;
		} catch (Exception ex) {
			throw new RuntimeException("ReplacementSelector could not be determined, verify the configuration file", ex);
		}
    }

    public static Mutation getMutationStrategy() {
    	
    	if(Config.mutationStrategy != null)
    		return Config.mutationStrategy;
    	
    	String strategy = Config.properties.getProperty("MutationStrategy");
    	
    	try {
    		Config.mutationStrategy = (Mutation)Class.forName(strategy).newInstance();
			return Config.mutationStrategy;
		} catch (Exception ex) {
			throw new RuntimeException("MutationStrategy could not be determined, verify the configuration file", ex);
		}
    }
    
    public static double getMutationRate() {
    	
    	if(Config.mutationRate != 0)
    		return Config.mutationRate;

    	try {
    		Config.mutationRate = Double.parseDouble(properties.getProperty("MutationRate"));
    		return Config.mutationRate;
    	}
    	catch(Exception ex) {
    		throw new RuntimeException("MutationRate could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getNumberReproductions() {
    	try {
    		return Integer.parseInt(properties.getProperty("NumberReproductions"));
    	}
    	catch (Exception ex) {
    		throw new RuntimeException("NumberReproductions could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static int getImmigrantEveryX() {
    	try {
    		return Integer.parseInt(properties.getProperty("ImmigrantEveryX"));
    	}
    	catch (Exception ex) {
    		throw new RuntimeException("ImmigrantEveryX could not be determined, verify the configuration file", ex);
    	}
    }
    
    public static String getUserDefinedValue(String property) {

    	String value = properties.getProperty(property);
    	if(value == null)
    		throw new RuntimeException("Property "+ property +" could not be determined, verify the configuration file");
    	return value;
    }
    
    public static PopulationInitializer getPopulationInitializer() {
    	
    	if(Config.populationInitializer != null)
    		return Config.populationInitializer;
    	
    	String initializer = Config.properties.getProperty("PopulationInitializer");
    	
    	try {
    		Config.populationInitializer = (PopulationInitializer)Class.forName(initializer).newInstance();
			return Config.populationInitializer;
		} catch (Exception ex) {
			throw new RuntimeException("PopulationInitializer could not be determined, verify the configuration file", ex);
		}
    }
}