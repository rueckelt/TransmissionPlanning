package adaptation.geneticAlgo;

import java.util.Arrays;
import java.util.Random;

//import org.apache.commons.math3.distribution.NormalDistribution;





import java.util.Vector;

import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;
import adaptation.utils.Printer;

public class Population {
	
	private final int NOF_TRIES_INIT_MUTATE=10;

    private Vector<Individual> individuals;
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
    	individuals = new Vector<Individual>();//new Individual[size];
    	
    	if(size<1) return;
    	
    	if(initialize){
        	individuals.add(new Individual(config));	//first unchanged
//        	individuals[0].obeyConstraint(net);
    		for(int i = 1; i<size; i++){
    			Individual newIndividual = new Individual(config);
    			int tries=0;
    			while(tries<NOF_TRIES_INIT_MUTATE && containsEqualIndividual(newIndividual)){
    				newIndividual.mutate(1.0, true);	//enforce a change
    				tries++;
    			}
    			individuals.add(newIndividual);
//    			System.out.println("Population: craeted individual "+ newIndividual.toString());
//    			if(tries==NOF_TRIES_INIT_MUTATE)System.out.println("Population: failed to init novel individual "+i);
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
    	this(amp, (config.getActiveFlow().length>3?config.getActiveFlow().length:4) ,config, initialise);	//use at least a size of 4
    }    
    
    public Population(int size, Config config, boolean initialise) {
    	this(1, size, config, initialise);
    }  

    /* Getters */
    public Individual getIndividual(int index) {
        return getIndividuals().get(index);
    }

    public Individual getFittest() {
        Individual fittest=null;// = getIndividuals().get(0);
        // Loop through individuals to find fittest
//        for (int i = 0; i < getPopulationSize(); i++) {
        for(int i=0; i<individuals.size(); i++){
            if (fittest==null || fittest.getFitness() >= getIndividual(i).getFitness()) {
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
//    	if(index<populationSize)
//    	getIndividuals().set(index,indiv);
    	getIndividuals().add(indiv);
//    	else{
//    		System.out.println("Population: size is "+populationSize);
//    	}
    }
    
    public void print() {
    	for (int i = 0; i < getIndividuals().size(); i++) {
    		System.out.print("i: " + i + " - ");
    		//Printer.printInt(getIndividuals()[i].getComb().getComb());
    		////////System.out.println("i: " + i + " - " + Fitness.getFitness(getIndividuals()[i]));
    		// //Printer.printInt(individuals[i].getGenes());
    	}
    }

	public Vector<Individual> getIndividuals() {
		return individuals;
	}
	
	public boolean containsIndividual(Individual indiv){
		for(Individual i:getIndividuals()){
			if(indiv==i) return true;
		}
		return false;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public double getAmp() {
		return amp;
	}

	public Config getConfig() {
		return config;
	}

}
