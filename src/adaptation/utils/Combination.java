package adaptation.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/**
	 * 
	 * @param combVar
	 * @param config
	 */
	public Combination(Config config) {
		this.setConfig(config);
		int[] combVar = config.getInitGenes();						//this is the initial individual from long term schedule
		resultGlobal = new int[config.getFlowNum()];
		combGlobal = new int[config.getFlowNum()];
		int[] activeFlow = new int[config.getActiveFlowNum()];		//mapping: contains the original indices of the active flows
		int[] activeComb = new int[config.getActiveFlowNum()];
		
		// set those inactive networks to 0
		for (int flowIndex = 0, aF = 0; flowIndex < combVar.length && aF < config.getActiveFlowNum(); flowIndex++) {
			if (config.getActiveFlowBool()[flowIndex] > 0) {		//for all flows marked as active
				activeFlow[aF] = flowIndex;							//enter index into list 
				
				//if the corresponding network index is invalid or points to network 0 (=none?), set the flow to network 0 (=none?)
				if (combVar[flowIndex] < 1 || combVar[flowIndex] > config.getActiveNetworkBool().length || config.getActiveNetworkBool()[combVar[flowIndex] - 1] < 1) {
					activeComb[aF] = 0;
				} else {
					//else, set the flow to the actual network
					activeComb[aF] = combVar[flowIndex];
				}
				aF++;
			}
		}
		
		//after this, activeComb[flow] contains a flow-network assignment derived from long term plan  
		//with active flows only and valid+active networks only.
		
		setComb(activeComb);
//		updatePart();
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
		for (int netId = 0; netId < getConfig().getNetNum() + 1; netId++) { // (not used...no 0 id for network) contains network "0", in order to add the cost for dropped packets (assigned to 0)	
			/*** initialization balancer:
			 *  get basic information about the active flows only and do some renumbering
			 *  to work with this subset in the main step
			 *  
			 *  ***/
			
			int nofActiveFlowsForThisNet = getPart()[netId];	//pn[networkId] = number of active flows assigned to this network
			int cap = 0;
			if (netId != 0) {	//network 0 = none
				cap = getConfig().getCapReal()[netId-1];
			}
			////Printer.printInt("pN", getPart());
			int[] minReqThroughput = new int[nofActiveFlowsForThisNet];
			int[] maxThroughputLimit = new int[nofActiveFlowsForThisNet];
			int[] flowPriority = new int[nofActiveFlowsForThisNet];
			int[] flowId = new int[nofActiveFlowsForThisNet];
			updatePart();
			Balancer.pick(getConfig().getmQ(), minReqThroughput, getComb(), getConfig().getActiveFlow(), netId);	//set minimum throughput of active flows
			Balancer.pick(getConfig().getPrior(), flowPriority, getComb(),getConfig().getActiveFlow(), netId);	//set priority of active flows. TODO: use flowCriticality??
			Balancer.pick(getConfig().getMax(), maxThroughputLimit, getComb(),getConfig().getActiveFlow(), netId);	//set maximum throughput of acive flows
			Balancer.pickFlowId(flowId, getComb(), getConfig().getActiveFlow(), netId);	
			
			//the balancer assigns data flows to networks
			Balancer b = new Balancer(cap, nofActiveFlowsForThisNet, step, getConfig().getPriorSum(), getConfig());
			b.setMinReq(minReqThroughput);
			b.setPriority(flowPriority);
			b.calPriorCost();			//normalizes the priority by the sum
			b.setMax(maxThroughputLimit);
			b.setFlowId(flowId);
			//if (i == 1) //Printer.printInt("b-max", b.getMax());
			/*** initialization ends ***/
			if (netId > 0) {
//				b.allocate2(netId-1, t);
				b.allocate(netId-1, t);
				setCombCost(getCombCost() + b.evaluator(getConfig().getTime(), netId-1));
			} 
			// copy the result in balancer (results with one network) to comb
			// eg. network1: b.result=[10,10,20]
			// comb = [2, 1, 1, 2, 1] ( active flows )
			// result = [?, 10, 10, ?, 20]
			int rIndex = 0;
			for (int j = 0; j < comb.length; j++) {
				if (comb[j] != 0 && comb[j] == netId) {
					getResult()[j] = b.getResult()[rIndex];
					rIndex++;
					if (rIndex >= nofActiveFlowsForThisNet) {
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
	

	//is called only for the fittest individual after finishing and updates stateful reward of cs
	public void updateState() {
		// bug - no should be good
		for (int f : getConfig().getActiveFlow()) {
			getConfig().getCs().updateStatefulReward(f, getConfig().getTime(), getResultGlobal()[f]);
		}

	}
//
//	public static void assignResult(int[] result, int[] subResult) {
//		// for (int i = 0; i < )
//	}
	
	/**
	 * for each network
	 * what is part?
	 * part is updated to an array that contains the count of active data flows which are assigned to this network for the individual 
	 * part[network]=number of active data flows assigned to this network
	 */
	public void updatePart() {
		int[] partNew = new int[getConfig().getNetNum() + 1]; // add network 0 (this represents unavailable network)
		
		for (int f = 0; f < comb.length; f++) {
			if (comb[f] < 1) partNew[0] += 1;//continue;	if a flow is not assigned to any network, count it for network 0 (=none)
			else partNew[comb[f]] += 1;					//	else assign it to the corresponding network
		}
		//after this part new contains the count of active data flows which are assigned to this network for the individual 
		setPart(partNew);
	}


	public int[] getComb() {
		return comb;
	}

	/**
	 * 
	 * @param comb set a combination = activeFlow[flowIndex] = networkAssignment
	 */
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
//	public int getRandomGene() {
//		Set<Integer> genePool = getConfig().getAvailableNetworks();
//
//		int item = genePool.size() == 0? 0 : new Random().nextInt(genePool.size()); // In real life, the Random object should be rather more shared than this
//		int i = 0;
//		for(Integer gene : genePool) {
//		    if (i == item)
//		        return gene;
//		    i = i + 1;
//		}
//		return 1;
//	}
	
//	//unused
//    public void obeyConstraint(int net, int[] com) {
//    	//HashMap<Integer, Set<Integer>> netTypeSet = getGeneType();
//    	int[] genetype = getConfig().getNetworkType().clone();
//    	int nt = 0;
//    	if (net - 1 < genetype.length && net - 1 >= 0) {
//    		nt =  genetype[net - 1];
//    	} else {
//    		return;
//    	}
//    	
//    	for (int i = 0; i < com.length; i++) {
//    		if (com[i] - 1 < 0) continue;
//    		if (com[i] == net) continue;
//    		if (genetype[com[i] - 1] == nt) {
//    			////Printer.printInt("type: ", genetype);
//    			////////System.out.println("obeyConstraint: " + "f_" + i + "orig_" + com[i] + "replace_" + net);
//    			com[i] = net;
//    		}
//    	}   	
//    }
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
}