package adaptation.geneticAlgo;

import adaptation.geneticAlgo.Individual;
import adaptation.geneticAlgo.Population;
import adaptation.utils.Combination;
import adaptation.utils.Printer;

public class Algorithm {
	
    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 1;
    private static int tournamentSize;
    private static final boolean elitism = true;

    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
    	////////System.out.println("in evolve pop_size: " + pop.getPopulationSize() + " - " + pop.getAmp());
        Population newPopulation = new Population(pop.getAmp(), pop.getConfig(), false);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
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

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(indiv1.getComb().getConfig());
      //  //Printer.printInt(newSol.getComb().getComb());
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


    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create an empty tournament population
        Population tournament = new Population(getTournamentSize(), pop.getConfig(), false);
        
        // For each place in the tournament get a random individual
        for (int i = 0; i < getTournamentSize(); i++) {
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
    

    
    public static void main(String[] args) {
 //   	Population pop = new Population(2,  true);
 //   	Individual newInd = Algorithm.crossover(pop.getIndividual(0), pop.getIndividual(1));
    	////////System.ot.println("new life: ");
    	//Printer.printInt(newInd.getComb().getComb());
    	////////System.out.println("fitness - newInd: " + Fitness.getFitness(newInd));
    }

	public static int getTournamentSize() {
		return tournamentSize;
	}

	public static void setTournamentSize(int tournamentSize) {
		Algorithm.tournamentSize = tournamentSize;
	}

}
