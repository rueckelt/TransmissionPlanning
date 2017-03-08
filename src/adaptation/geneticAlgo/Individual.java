package adaptation.geneticAlgo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import adaptation.geneticAlgo.Fitness;
import adaptation.utils.Combination;
import adaptation.utils.Config;
import adaptation.utils.Hasher;
import adaptation.utils.Printer;
import ToolSet.CostSeparation;
import ToolSet.JsonLogger;
import ToolSet.LogMatlabFormat;

//import org.apache.commons.math3.distribution.NormalDistribution;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import visualization.Plot;
import visualization.VisualizationPack;

public class Individual extends Thread implements Comparable {
    private Combination comb;
    //private static Combination combInit;
    private double fitness = Double.MAX_VALUE;
    
    //int defaultGeneLength = comb.getConfig().getFlowNum(); // mutation index + 5 flows
    private int genes[];
    private int[] initGenes; // TODO shall be put in population
    private int mutateIndex = -1;
    private static Set<Integer> genePool;
    private int id = -1;
    
//    public Individual() {
//    }
    
    public Individual(Config config) {
    	genes = new int[config.getFlowNum()];
    	Arrays.fill(genes, 1);		//set each flow to network 1?
//    	Arrays.fill(initGenes, 1);
    	comb = new Combination(config);
    //	////////System.out.println("indi: ");
    //	//Printer.printInt(comb.getComb());
 //   	comb.setComb(initGenes);
    	comb.updatePart();
    }
    
    public Individual(Combination combVar) {
 //   	Arrays.fill(initGenes, 1);
    	setComb(combVar);
    	genes = combVar.getComb();
    }
    
    
    // Create a random individual
    public void generateIndividual(int netIndex) {
    	mutate(false, netIndex);
    	
    }
    
    
//   public void generateInit() {
//    	Combination comb = new Combination(initGenes);
//    	setCombInit(comb);
////    	//Printer.printInt(combInit.getComb());
////    	//Printer.printInt(combInit.getPart());
// 
//    }
    
//    public static void generateInit(int[] initLongTerm) {
//    	initGenes  = initLongTerm;
//    }
    
    public void mutate(boolean random, int netIndex) {
    	////////System.out.println("in mutate: + " + netIndex);
		int[] combList = comb.getComb().clone();
		// for (int i = 0; i < 5 && combList.equals(comb.getComb()); i++) {
			HashMap<Integer, List<Integer>> map = Hasher.listToMap(combList);
			////////System.out.println(map.toString());
			List<Integer> net = map.get(netIndex);
			if (net == null) {
				if (map.get(0) != null) {
					net = map.get(0);
				} else {
					return;
				}
			}
			int randomFlow = -1;
		    ////////System.out.println("++++++++++++++++++++++++++");
		int randomNet = 0;
		for (int i = 0; i < 100 && Arrays.equals(combList, comb.getComb()); i++) {

			Random rand = new Random();
		    int randomFlowIndex = rand.nextInt(net.size());		
		    randomFlow = net.get(randomFlowIndex); 				//select random flow
		    randomNet = getRandomGene();						
		    if (getGenePool().size() > 1) {
			    ////////System.out.println(getGenePool().toString());
			    ////////System.out.println("combList: " + randomFlow + ":" + combList[randomFlow]);

			    combList[randomFlow] = randomNet;
			    ////////System.out.println("combList: " + randomFlow + ":" + randomNet);
			    ////////System.out.println("*********************");
		    }
		    combList[randomFlow] = randomNet;



		}
	    if (!random) {
	    	mutateIndex = randomFlow;
	    }
	    ////Printer.printInt(combList);	

	    comb.setComb(combList);
	    comb.updatePart();
	    obeyConstraint(randomNet);
	    //////////System.out.println("mutateIndex: " + getMutateIndex());
    }
    
//    public void mutate() {
//    	// ////////System.out.println("this mutate");
//		int[] combList = comb.getComb().clone();
//		int randomFlow = -1;
//		int randomFlowIndex = -1;
//		int randomNet = -1;
//		for (int i = 0; i < 100 && Arrays.equals(combList, comb.getComb()); i++) {
//
//			Random rand = new Random();
//			randomFlow = rand.nextInt(comb.getComb().length);
//		    randomNet = getRandomGene();
//		    
//		 //   ////////System.out.println("randomNet: " + randomNet);
//		    combList[randomFlow] = randomNet;
//		 //   System.out.print("i: " + i + " ");
//		 //   //Printer.printInt(combList);	
//		}
//	    ////Printer.printInt(combList);	
//
//	    comb.setComb(combList);
//	    comb.updatePart();
//	    //////////System.out.println("mutateIndex: " + getMutateIndex());
//    }
    
    /**
     * mutate the individual
     * give it 100 tries to mutate, then give up if no change happened.
     * 
     * oldMutationStrategy:
     * select a random flow and assign random network
     * 
     * newMutationStrategy:
     * for each flow, assign a random network with a proability of
     * (x+1) / population size.
     * hereby, x is the log10 of the repetition try (maximum 100 tries, see above)
     * 
     */
    public void mutate() {
    	boolean newMutationStrategy = true;
    	// ////////System.out.println("this mutate");
		int[] combMutated = comb.getComb().clone();	//copy
		int randomFlow = -1;
		int randomNet = -1;
		
		//while no change happened or 100 runs exceeded
		for (int i = 0; i < 100 && Arrays.equals(combMutated, comb.getComb()); i++) {	
			
			
			if(newMutationStrategy){
				//change each gene (flow) with a probability of 1/populationSize
				for(int f=0; f<comb.getComb().length;f++){
					//TODO: check impact of the + Math.log10(i+1). Increase
					boolean doChange = Math.random() < ((1.0+Math.log10(i+1)) / (double)comb.getComb().length);
					if(doChange){
						combMutated[f]=getRandomGene();
					}
				}
			}
			else{
				Random rand = new Random();
				randomFlow = rand.nextInt(comb.getComb().length);
			    randomNet = getRandomGene();
			    
			 //   ////////System.out.println("randomNet: " + randomNet);
			    combMutated[randomFlow] = randomNet;
			 //   System.out.print("i: " + i + " ");
			 //   //Printer.printInt("combList: ", combList);	
			 //   //////System.out.println("randomNet: " + randomNet);
			}
		}
	    ////Printer.printInt(combList);	

	    comb.setComb(combMutated);
	    comb.updatePart();
	    obeyConstraint(randomNet);
	    ////Printer.printInt("nettype", getGeneTypeArray());
	    ////Printer.printInt("after obey: ", comb.getComb());
	    //////////System.out.println("mutateIndex: " + getMutateIndex());
    }
    
    public HashMap<Integer, Set<Integer>> getGeneType() {
    	return comb.getConfig().getNetTypeMap();
    }
    
    public int[] getGeneTypeArray() {
    	return comb.getConfig().getNetworkType();
    }
    
    // this is the type constraint that flow-net Config must obey
    public void obeyConstraint(int net) {
    	int[] com = comb.getComb().clone();
    	//HashMap<Integer, Set<Integer>> netTypeSet = getGeneType();
    	int[] genetype = getGeneTypeArray().clone();
    	int nt = 0;
    	if (net - 1 < genetype.length && net - 1 >= 0) {
    		nt =  genetype[net - 1];
    	} else {
    		return;
    	}
    	
    	for (int i = 0; i < com.length; i++) {
    		if (com[i] - 1 < 0) continue;
    		if (com[i] == net) continue;
    		if (genetype[com[i] - 1] == nt) {
    			////Printer.printInt("type: ", genetype);
    			////////System.out.println("obeyConstraint: " + "f_" + i + "orig_" + com[i] + "replace_" + net);
    			com[i] = net;
    		}
    	}
    	comb.setComb(com); // update Config info
    	comb.updatePart(); // not good implementation, but please read the comment in Config.java
    	
    }
    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
//    public static void setDefaultGeneLength(int length) {
//        defaultGeneLength = length;
//    }
    
    public double getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int i) {
        genes[index] = i;
    }

    /* Public methods */
    public int size() {
        return comb.getComb().length;
    }

    public double getFitness() {
        fitness = Fitness.getFitness(this);
       
        return fitness;
    }
    
    public int[] getGenes() {
    	return genes;
    }
//    
//    public static int[] getInitGenes() {
//    	return initGenes;
//    }
    
    public static int getRandom(int start, int end) {
        Random rand = new Random();
        return rand.nextInt((end - start) + 1) + start;
    }	
	
	public double calcViolation() {
		return 0.0;
	}
	
	public void print() {
		//Printer.printInt(this.getComb().getComb());
	}

	public Combination getComb() {
		return comb;
	}

	public void setComb(Combination comb) {
		this.comb = comb;
	}

	public int getMutateIndex() {
		return mutateIndex;
	}

	public void setMutateIndex(int mutateIndex) {
		this.mutateIndex = mutateIndex;
	}

//	public static Combination getCombInit() {
//		return combInit;
//	}
//
//	public static void setCombInit(Combination combInit) {
//		Individual.combInit = combInit;
//	}
	
	public int getRandomGene() {
		int item = getGenePool().size() == 0? 0 : new Random().nextInt(getGenePool().size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		for(Integer gene : getGenePool()) {
		    if (i == item)
		        return gene;
		    i = i + 1;
		}
		return 0;
	}
	
	public int getCertainGene(int item) {
		int i = 0;
		for(Integer gene : getGenePool()) {
		    if (i == item)
		        return gene;
		    i = i + 1;
		}
		return 1;
	}

	public Set<Integer> getGenePool() {
		//Config.getAvailableNetworks().add(0);
		setGenePool(getComb().getConfig().getAvailableNetworks());
		return genePool;
	}

	public void setGenePool(Set<Integer> genePool) {
		this.genePool = genePool;
	}
	
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("[");
//		int[] d = getComb().getComb();
//		for (int i = 0; i < d.length; i++) {
//			if (i != d.length - 1) {
//				sb.append(d[i]);
//				sb.append(',');
//			} else {
//				sb.append(d[i]);
//			}
//		}
//		sb.append(']');
//		return sb.toString();
//	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Individual d2 = (Individual) o;
		if (this.getFitness() - d2.getFitness() < 0) {
			return -1;
		} else if (this.getFitness() - d2.getFitness() > 0) {
			return 1;
		}
		return 0;
	}
	
	 public void run() {
		 getFitness();
		// ////////System.out.println(id + ": " + );
	}

	public int getIndivId() {
		return id;
	}

	public void setIndivId(int id) {
		this.id = id;
	} 



}
