package adaptation.geneticAlgo;
/****
 * This is the main function of GA. 
 * GA contains: Individual (chromosome), population, fitness, algorithm
 * Individual.java: defines chromosomes - which uses actually Combination.java instance 
 * Population.java: defines the process to envolve a pop
 * Fitness.java: it's the rating of each solution - which refers to a value in Combination.java
 * Algorithm.java: defines the details of tournament, crossover and mutate.
 */
import java.util.ArrayList;

import adaptation.utils.Combination;
import adaptation.utils.Config;
import adaptation.utils.Printer;
import ToolSet.CostSeparation;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;

//import netModel.Environment;

public class GeneticAlgo {

    private final double uniformRate = 0.5;
	
	private final boolean elitism = true;
//    private final double mutationRate = 1;
	
	/**
	 * Function: here does some configuration of GA and is used to run GA 
	 * @param popAmp - it's the factor of population size. popSize = popAmp * chromosome.length 
	 * @return
	 */
	public Individual run(double popAmp, Config config) {
		if(config.getActiveFlowNum()<1)return new Individual(config);	//when there is not active flow
		ArrayList<Double> converge = new ArrayList<Double>();

        // Set a candidate solution
        // Create an initial population - with initial solution
        Population myPop = new Population(popAmp, config, true); // true means this is for pop initialization

        // Tournament Config
        if(myPop.getPopulationSize()<1) return new Individual(config);	//no population

        Individual bestInd = new Individual(config); // bestInd is the best Solution 
        ArrayList<Population> pop = new ArrayList<>(); // for convergeInfo

        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        int generationMax = 300;
        while (generationCount < generationMax){//Math.min(generationMax, myPop.size() * 50)) { // the naive stop criteria
            generationCount++;
            if (myPop.size() != 0) {
            //////////System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
	            if (bestInd.getFitness() > myPop.getFittest().getFitness()) {
	            	bestInd = myPop.getFittest();	//update with individual of lowest cost
	            }
	           
	            pop.add(myPop);
	            converge.add(bestInd.getFitness());
//	            System.out.println("Fitness in rnd "+generationCount+" is "+bestInd.getFitness());
	            ////Printer.printInt(myPop.getFittest().getComb().getComb());
	            myPop = evolvePopulation(myPop);
            } else {
            	converge.add(0.0);
            }            
        }

        if (myPop.size() == 0) return bestInd;
        
        //for the fittest, update stateful reward in costSeparation
        bestInd.getComb().updateState();
        
        return myPop.getFittest();
		
	}

    /* Public methods */
    
    // Evolve a population
    private Population evolvePopulation(Population pop) {
    	////////System.out.println("in evolve pop_size: " + pop.getPopulationSize() + " - " + pop.getAmp());
        Population newPopulation = new Population(pop.getAmp(), pop.getConfig(), false);


        int elitismOffset=0;
        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
            elitismOffset = 1;
        }

        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.getPopulationSize(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }
        ////////System.out.println("newpop: " + newPopulation.getPopulationSize());
        
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.getPopulationSize(); i++) {
        	////////System.out.println("i: " + i);
//            mutate(, true);
            newPopulation.getIndividual(i).mutate();
        }

        return newPopulation;
    }
    

    // Select individuals for crossover
    private Individual tournamentSelection(Population pop) {
    	int tournamentSize= Math.max(2, pop.getPopulationSize());
        // Create an empty tournament population
        Population tournament = new Population(tournamentSize, pop.getConfig(), false);
        
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
        	//find an unused random individual	TR: added while loop to select new individual
        	Individual selectedIndiv;
        	do{
        		int randomId = (int) (Math.random() * pop.getPopulationSize());
        		selectedIndiv = pop.getIndividual(randomId);
        	} while(tournament.containsIndividual(selectedIndiv));	//repeat if individual was already selected.
        	
            tournament.saveIndividual(i, selectedIndiv);	//add selected Individual to set
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
    
    // Crossover individuals
    private Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(indiv1.getComb().getConfig());

        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.getComb().getComb()[i] = indiv1.getComb().getComb()[i];
            } else {
                newSol.getComb().getComb()[i] = indiv2.getComb().getComb()[i];
            }
            newSol.obeyConstraint(newSol.getComb().getComb()[i]);
            ////Printer.printInt("\ncrossover + " + i + ": ", newSol.getComb().getComb());
        }
        //newSol.getComb().updatePart();
        return newSol;
        // [1 2 2 4 4] - [1 3 3 5 ] - 1 2 3 4 5  - 1 2 3
    }

}
