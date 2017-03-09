package adaptation.geneticAlgo;

import java.util.Arrays;
import java.util.Random;

//import org.apache.commons.math3.distribution.NormalDistribution;





import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;
import adaptation.utils.Printer;

public class Population {
	
	private final int NOF_TRIES_INIT_MUTATE=10;

    private Individual[] individuals;
 //   private int[] seed = new int[Individual.defaultGeneLength]; // the long-term subplan
    private int populationSize;
    private Config config;
    private double amp;

    /*
     * Constructors
     */
    // Create a population
    
    public Population(double amp, int size, Config config, boolean initialize){
    	this.config= config;
    	this.amp=amp;
    	populationSize=size;
    	individuals = new Individual[getPopulationSize()];
    	
    	if(size<1) return;
    	
    	if(initialize){
        	individuals[0] = new Individual(config);	//first unchanged
//        	individuals[0].obeyConstraint(net);
    		for(int i = 1; i<size; i++){
    			Individual newIndividual = new Individual(config);
    			int tries=0;
    			while(tries<NOF_TRIES_INIT_MUTATE && containsEqualIndividual(newIndividual)){
    				newIndividual.mutate(1.0, true);	//enforce a change
    				tries++;
    			}
    			individuals[i]=newIndividual;
    			System.out.println("Population: craeted individual "+ newIndividual.toString());
    			if(tries==NOF_TRIES_INIT_MUTATE)System.out.println("Population: failed to init novel individual "+i);
    		}
    	}
    }
    
    private boolean containsEqualIndividual(Individual indiv){
    	for(Individual i0:individuals){
    		if(i0!=null && Arrays.equals(indiv.getComb().getComb(),i0.getComb().getComb())){
    			return true;
    		}
    	}
    	return false;
    }
    
    public Population(double amp, Config config, boolean initialise) {
    	this(amp, (config.getActiveFlow().length>3?config.getActiveFlow().length:4) ,config, initialise);
    }    
    
    public Population(int size, Config config, boolean initialise) {
    	this(1, size, config, initialise);
    }  

    /* Getters */
    public Individual getIndividual(int index) {
        return getIndividuals()[index];
    }

    public Individual getFittest() {
        Individual fittest = getIndividuals()[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < getPopulationSize(); i++) {
            if (fittest.getFitness() >= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            	//fittest.setIndivId(i);
            	//fittest.run();
            }
            //////////System.out.println("fittest: " + fittest.getFitness());
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return getConfig().getActiveFlowNum();//getIndividuals().length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        getIndividuals()[index] = indiv;
    }
    
    public void print() {
    	for (int i = 0; i < getIndividuals().length; i++) {
    		System.out.print("i: " + i + " - ");
    		//Printer.printInt(getIndividuals()[i].getComb().getComb());
    		////////System.out.println("i: " + i + " - " + Fitness.getFitness(getIndividuals()[i]));
    		// //Printer.printInt(individuals[i].getGenes());
    	}
    }

	public Individual[] getIndividuals() {
		return individuals;
	}
	
	public boolean containsIndividual(Individual indiv){
		for(Individual i:getIndividuals()){
			if(indiv==i) return true;
		}
		return false;
	}

	public void setIndividuals(Individual[] individuals) {
		this.individuals = individuals;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public double getAmp() {
		return amp;
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
