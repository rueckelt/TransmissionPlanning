package adaptation.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;



import adaptation.tabusearch.Solution;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;
import ToolSet.CostSeparation;

/* Combination - means the network-flow map */

public class Combination {

	/*** important vairables ***/
	private Config config;
	private int[] part; // records how many flows are assigned to network, length -> the number of networks
	// how many packets are assigned to flows, combined with comb[], the concrete allocation plan can be known. length -> the number of flow
	private int[] resultGlobal;
	// flow-net mapping, each entry index - flow, the value is the index of network, length -> the number of flows
	private int[] combGlobal;
	private int[] comb; // only active flows (for each time slot, the active flows are different)
	private int[] result; // only results of active flows
	/*** important variables ends ***/

	private double combCost = -1;
	
	public Combination(int[] combVar, Config config) {
		this.setConfig(config);
		resultGlobal = new int[config.getFlowNum()];
		combGlobal = new int[config.getFlowNum()];
		int[] activeFlow = new int[config.getActiveFlowNum()];
		int[] activeComb = new int[config.getActiveFlowNum()];
		// set those inactive networks to 0
		for (int i = 0, aF = 0; i < combVar.length && aF < config.getActiveFlowNum(); i++) {
			if (config.getActiveFlowBool()[i] > 0) {
				activeFlow[aF] = i;
				////Printer.printInt("comb", combVar);
				//////////System.out.println("i: " + i + " - " + combVar[i]);
				if (combVar[i] < 1 || combVar[i] > config.getActiveNetworkBool().length || config.getActiveNetworkBool()[combVar[i] - 1] < 1) {
					activeComb[aF] = 0;
				} else {
					activeComb[aF] = combVar[i];
				}
				aF++;
			}
		}
		setComb(activeComb);
		updatePart();
		setResult(new int[comb.length]);
	}
//	public Combination(int[] combVar) {
//
//		resultGlobal = new int[getConfig().getFlowNum()];
//		combGlobal = new int[getConfig().getFlowNum()];
//		int[] activeFlow = new int[getConfig().getActiveFlowNum()];
//		int[] activeComb = new int[getConfig().getActiveFlowNum()];
//		// set those inactive networks to 0
//		for (int i = 0, aF = 0; i < combVar.length && aF < getConfig().getActiveFlowNum(); i++) {
//			if (getConfig().getActiveFlowBool()[i] > 0) {
//				activeFlow[aF] = i;
//				////Printer.printInt("comb", combVar);
//				//////////System.out.println("i: " + i + " - " + combVar[i]);
//				if (combVar[i] < 1 || combVar[i] > getConfig().getActiveNetworkBool().length || getConfig().getActiveNetworkBool()[combVar[i] - 1] < 1) {
//					activeComb[aF] = 0;
//				} else {
//					activeComb[aF] = combVar[i];
//				}
//				aF++;
//			}
//		}
//		setComb(activeComb);
//		updatePart();
//		setResult(new int[comb.length]);
//	}

	public Combination() {
		// TODO Auto-generated constructor stub
		//////System.out.println("flowNum: " + config.getFlowNum());
		comb = new int[getConfig().getActiveFlowNum()];
		setResult(new int[comb.length]);
//		updatePart();
	}

	public double run() {
		int t = getConfig().getTime();
		setCombCost(0);
		int step = 1;
		getConfig().extend(getCombGlobal(), getComb(), getConfig().getActiveFlow());
		// do Balancer (allocation work) for each network
		for (int i = 0; i < getConfig().getNetNum() + 1; i++) { // (not used...no 0 id for network) contains network "0", in order to add the cost for dropped packets (assigned to 0)	
			/*** initialization balancer ***/
			int pN = getPart()[i];
			int cap = 0;
			if (i != 0) {
				cap = getConfig().getCapReal()[i-1];
			}
			////Printer.printInt("pN", getPart());
			int[] minReq = new int[pN];
			int[] maxReq = new int[pN];
			int[] p2 = new int[pN];
			int[] flowId = new int[pN];
			updatePart();
			Balancer.pick(getConfig().getmQ(), minReq, getComb(), getConfig().getActiveFlow(), i);
			Balancer.pick(getConfig().getPrior(), p2, getComb(),getConfig().getActiveFlow(), i);
			Balancer.pick(getConfig().getMax(), maxReq, getComb(),getConfig().getActiveFlow(), i);
			Balancer.pickFlowId(flowId, getComb(), getConfig().getActiveFlow(), i);
			Balancer b = new Balancer(cap, pN, step, getConfig().getPriorSum(), getConfig());
			b.setMinReq(minReq);
			b.setPriority(p2);
			b.calPriorCost();
			b.setMax(maxReq);
			b.setFlowId(flowId);
			//if (i == 1) //Printer.printInt("b-max", b.getMax());
			/*** initialization ends ***/
			if (i > 0) {
				b.allocate(i-1, t);
				setCombCost(getCombCost() + b.evaluator(getConfig().getTime(), i-1));
			} 
			// copy the result in balancer (results with one network) to comb
			// eg. network1: b.result=[10,10,20]
			// comb = [2, 1, 1, 2, 1] ( active flows )
			// result = [?, 10, 10, ?, 20]
			int rIndex = 0;
			for (int j = 0; j < comb.length; j++) {
				if (comb[j] != 0 && comb[j] == i) {
					getResult()[j] = b.getResult()[rIndex];
					rIndex++;
					if (rIndex >= pN) {
						break;
					}
				}
			}
		}
	
		getConfig().extend(getResultGlobal(), getResult(), getConfig().getActiveFlow());
		getConfig().extend(getCombGlobal(), getComb(), getConfig().getActiveFlow());
	//	//Printer.printInt("global result", getResultGlobal());
	//	updateState();
		return getCombCost();
	}
	

	public void updateState() {
		// bug
		for (int f : getConfig().getActiveFlow()) {
			getConfig().getCs().updateStatefulReward(f, getConfig().getTime(), getResultGlobal()[f]);
		}

	}

	public static void assignResult(int[] result, int[] subResult) {
		// for (int i = 0; i < )
	}
	
	public void updatePart() {
		int[] partNew = new int[getConfig().getNetNum() + 1]; // add network 0 (this represents unavailable network)
		for (int i = 0; i < comb.length; i++) {
			if (comb[i] < 1) partNew[0] += 1;//continue;
			else partNew[comb[i]] += 1;
		}
		setPart(partNew);
	}


	public int[] getComb() {
		return comb;
	}

	public void setComb(int[] comb) {
		this.comb = comb;
		updatePart();
	}

	public int[] getPart() {
		return part;
	}

	public void setPart(int[] part) {
		this.part = part;
	}
	
	public int[] getResultGlobal() {
		return resultGlobal;
	}



	public void setResultGlobal(int[] resultGlobal) {
		this.resultGlobal = resultGlobal;
	}



	public int[] getCombGlobal() {
		return combGlobal;
	}



	public void setCombGlobal(int[] combGlobal) {
		this.combGlobal = combGlobal;
	}

	public double getCombCost() {
		return combCost;
	}
	
	public int[] getResult() {
		return result;
	}

	public void setResult(int[] result) {
		this.result = result;
	}



	public void setCombCost(double combCost) {
		this.combCost = combCost;
	}

	/**
	 * The following override methods are for Tabu-search, not necessary
	 */
	/*
	@Override
	public Double getValue() {	
		//if (run() < 0) return Double.MAX_VALUE;
		return run();
	}

	
	@Override
	public List<Solution> getNeighbors() {
		// TODO Auto-generated method stub
		List<Solution> neigh = new ArrayList<Solution>();
		Random random = new Random();
		Integer neighborsCount = random.nextInt(7) + 3; 
		Set<Integer> genePool = config.getAvailableNetworks();
		for (int j = 0; j < neighborsCount; j++) { 
		    int[] comb = this.getComb().clone();
		    if (j < comb.length) {
		    	int net = getRandomGene();
		    	comb[j] = net;
		    	obeyConstraint(net, comb);
		    	neigh.add(new Combination(comb));
		    }
		} 
		return neigh;
	}
*/	
	public int getRandomGene() {
		Set<Integer> genePool = getConfig().getAvailableNetworks();

		int item = genePool.size() == 0? 0 : new Random().nextInt(genePool.size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		for(Integer gene : genePool) {
		    if (i == item)
		        return gene;
		    i = i + 1;
		}
		return 1;
	}
	
    public void obeyConstraint(int net, int[] com) {
    	//HashMap<Integer, Set<Integer>> netTypeSet = getGeneType();
    	int[] genetype = getConfig().getNetworkType().clone();
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
    }
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
}