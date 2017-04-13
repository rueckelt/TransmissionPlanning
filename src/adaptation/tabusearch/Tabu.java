package adaptation.tabusearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;

public class Tabu {
	
	private ArrayList<int[]> tabuList = new ArrayList<int[]>();	
	
	public Individual run(Config config) {
		if(config.getActiveFlowNum()<1)return new Individual(config);	//when there is not active flow
		ArrayList<Double> converge = new ArrayList<Double>();

		Individual best = new Individual(config);	//load from initial plan
		addToTabuList(best);
		
		int generations = 500;
		while(generations>0){
			Individual ind = new Individual(best.getComb().getConfig());
			
			generations--;
		}
		return best;
	}

	private void addToTabuList(Individual ind){
		tabuList.add(ind.getComb().getComb());
	}
	
	private boolean inTabuList(Individual ind){
		for(int[] comb : tabuList){
			if(Arrays.deepEquals(comb, ind.getComb().getComb())){
				
			}
		}
		
	}
	
}

