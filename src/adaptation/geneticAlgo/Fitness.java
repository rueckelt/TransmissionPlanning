package adaptation.geneticAlgo;

import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;

public class Fitness {

	// 
    static int[] solution = new int[11];

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static double getFitness(Individual individual) {
    	individual.getComb().updatePart();
    	double combcost = individual.getComb().getCombCost();
    	if (combcost == -1) {
            return individual.getComb().run(); //< 0? Double.MAX_VALUE: individual.getComb().run();
    	} else {
    		return combcost;
    	}
    }    
}
