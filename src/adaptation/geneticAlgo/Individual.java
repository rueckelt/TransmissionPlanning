package adaptation.geneticAlgo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import adaptation.utils.Combination;
import adaptation.utils.Config;
//import org.apache.commons.math3.distribution.NormalDistribution;

public class Individual extends Thread {
    private Combination comb;
    Config config;

    
    public Individual(Config config) {
    	this.config=config;
    	comb = new Combination(config);
    	comb.updatePart();
    }

    
    /**
     * mutate the individual
     * give it 100 tries to mutate, then give up if no change happened.
     * 
     * oldMutationStrategy:
     * select a random flow and assign random network
     * 
     * newMutationStrategy:
     * for each flow, assign a random network with a proability of
     * amp / population size.
     * 
     */
    public void mutate(double amplifier, boolean force) {
    	boolean newMutationStrategy = true;
    	// ////////System.out.println("this mutate");
		int[] combMutated = comb.getComb().clone();	//copy
		int randomFlow = -1;
		int randomNet = -1;
		
		int tries =1;
		if(force){
			tries=20;
		}
		Vector<Integer> netsToObey = new Vector<Integer>();
		//while no change happened or 100 runs exceeded
		for (int i = 0; i < tries && Arrays.equals(combMutated, comb.getComb()); i++) {	
			
			
			if(newMutationStrategy){
				//change each gene (flow) with a probability of 1/populationSize
				for(int f=0; f<comb.getComb().length;f++){
					//TODO: check impact of the + Math.log10(i+1). Increase
					boolean doChange = Math.random() < (amplifier / (double)comb.getComb().length);
					if(doChange){
						int net = getRandomGene();
						if(net !=combMutated[f]){
							netsToObey.add(net);
							combMutated[f]=net;
							combMutated=obeyConstraint(combMutated, net);
							break;
						}
						
					}
				}
			}
			else{
				Random rand = new Random();
				randomFlow = rand.nextInt(comb.getComb().length);
			    randomNet = getRandomGene();
			    combMutated[randomFlow] = randomNet;

				if(randomNet !=combMutated[randomFlow]){
					combMutated=obeyConstraint(combMutated, randomNet);
				}
			}
			tries--;
		}


	    comb.setComb(combMutated);
	    comb.updatePart();
    }
    
    public void mutate(){
    	mutate(0.5, false);
    }
    
    public HashMap<Integer, Set<Integer>> getGeneType() {
    	return comb.getConfig().getNetTypeMap();
    }

    
    public void obeyConstraint(int net){
    	
    	int[] assignedNets=obeyConstraint(comb.getComb(), net);
    	comb.setComb(assignedNets); // update Config info
    	comb.updatePart(); // not good implementation, but please read the comment in Config.java
    }
    // this is the type constraint that flow-net Config must obey
    public int[] obeyConstraint(int[] assignedNets, int net) {
//    	int[] assignedNets = comb.getComb();
    	
    	//get type of each network
    	int[] netTypes = comb.getConfig().getNetworkType().clone();
//    	System.out.println("NET TYPES = "+Arrays.toString(netTypes));
    	//get type of net
    	int netType = 0;
    	if (net - 1 < netTypes.length && net - 1 >= 0) {
    		netType =  netTypes[net - 1];
    	} else {
    		return assignedNets;
    	}
    	
    	//for each flow in the combination do 
    	for (int f = 0; f < assignedNets.length; f++) {
    		if (assignedNets[f] - 1 < 0) continue;	//do nothing when no network is assigned
    		if (assignedNets[f] == net) continue;	//do nothing if same network is assigned
    		if (netTypes[assignedNets[f] - 1] == netType) {	//when network types match, assign this net
    			////Printer.printInt("type: ", genetype);
//    			System.out.println("Individual: obeyConstraint: " + "f_" + f + ", orig_" + assignedNets[f] + ", replace_" + net);
    			assignedNets[f] = net;
    		}
    	}
//    	comb.setComb(assignedNets); // update Config info
//    	comb.updatePart(); // not good implementation, but please read the comment in Config.java
    	return assignedNets;
    }


    /* Public methods */
    public int size() {
        return comb.getComb().length;
    }

    public double getFitness() {
    	getComb().updatePart();
    	double combcost = getComb().getCombCost();
    	if(combcost>=0) return combcost;
    	return getComb().getCost();

    }
    
	public Combination getComb() {
		return comb;
	}

	public void setComb(Combination comb) {
		this.comb = comb;
	}

	public int getRandomGene() {
		// In real life, the Random object should be rather more shared than this		
		if(config.getAvailableNetworks().size()==0) return 0;
		int index = config.getAvailableNetworks().size() == 0? 0 : new Random().nextInt(config.getAvailableNetworks().size()); 
		return config.getAvailableNetworks().get(index);
//		int i = 0;
//		for(Integer gene : config.getAvailableNetworks()) {
//		    if (i == index)
//		        return gene;
//		    i = i + 1;
//		}
//		return 0;
	}
	//check if the gene "item" is available. If yes, return it. else return 1. 
	public int getCertainGene(int item) {
		int i = 0;
		for(Integer gene : config.getAvailableNetworks()) {
		    if (i == item)
		        return gene;
		    i = i + 1;
		}
		return 1;
	}
	
	//junit test
	 public void run() {
		 getFitness();
	}
	 
	 
	 /**
	  * 
	  * @param distance between 0 and x. influences mutation rate. each gene is replaced by random with distance/chromosome_length
	  * @return new mutated individual
	  */
	 public Individual getNeighbor(double distance){
		 Individual ind = new Individual(config);
		 for(int i=0; i<getComb().getComb().length; i++){
			 ind.getComb().getComb()[i]=new Integer(getComb().getComb()[i]);
		 }
		 ind.mutate(distance, true);
		 return ind;
	 }

	public String toString(){
		return "Individual "+Arrays.toString(comb.getComb())+", Fitness="+getFitness();
	}

}
