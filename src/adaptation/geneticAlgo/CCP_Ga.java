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

public class CCP_Ga {
	//private Environment e;
	private static ArrayList<ArrayList<Double>> convergeList = new ArrayList<ArrayList<Double>>();// this is used to record converge info. Not neccessary for GA.
	private static ArrayList<ArrayList<Population>> popList = new ArrayList<>();// similar, not neccessary
	/**
	 * Function: here does some configuration of GA and is used to run GA 
	 * @param popAmp - it's the factor of population size. popSize = popAmp * chromosome.length 
	 * @return
	 */
	public static Individual run(double popAmp, Config config) {
		//setConverge(new ArrayList<Integer>());
		ArrayList<Double> converge = new ArrayList<Double>();

        // Set a candidate solution
        // Create an initial population - with initial solution
        Population myPop = new Population(popAmp, config, true); // true means this is for pop initialization
        int popSize = myPop.getPopulationSize();
        // Tournament Config
        if (popSize <= 2) {
        	Algorithm.setTournamentSize(popSize);
        } else {
        	Algorithm.setTournamentSize(Math.max(2, popSize/2));
        }
        Individual bestInd = new Individual(config); // bestInd is the best Solution 
        ArrayList<Population> pop = new ArrayList<>(); // for convergeInfo
        if (myPop.size() != 0) {
        //    converge.add(Individual.getCombInit().run());
        }
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        while (generationCount < myPop.size() * 50) { // the naive stop criteria
        	////////System.out.println("******************" + generationCount + "******************");
            generationCount++;
            if (myPop.size() != 0) {
            //////////System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
	            if (bestInd.getFitness() > myPop.getFittest().getFitness()) {
	            	bestInd = myPop.getFittest();
	            }
	            pop.add(myPop);
	            converge.add(bestInd.getFitness());
	            System.out.println("Fitness in rnd "+generationCount+" is "+bestInd.getFitness());
	            ////Printer.printInt(myPop.getFittest().getComb().getComb());
	            myPop = Algorithm.evolvePopulation(myPop);
            } else {
            	converge.add(0.0);
            }
            /*
            for (Individual ind : myPop.getIndividuals()) {
            	////////System.out.println(ind.toString());
            }
            */
        }
        convergeList.add(converge);

        if (myPop.size() == 0) return bestInd;
        getPopList().add(pop);
        
        bestInd.getComb().updateState();
        
        return myPop.getFittest();
		
	}

	public static ArrayList<ArrayList<Double>> getConvergeList() {
		return convergeList;
	}
	public static void setConvergeList(ArrayList<ArrayList<Double>> convergeList) {
		CCP_Ga.convergeList = convergeList;
	}
	public static ArrayList<ArrayList<Population>> getPopList() {
		return popList;
	}
	public static void setPopList(ArrayList<ArrayList<Population>> popList) {
		CCP_Ga.popList = popList;
	}
}
