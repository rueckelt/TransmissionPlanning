package adaptation.utils;
/***
 * Balancer deals with the subproblem 2.
 * determined: the network (capacity and the flows assigned to it) and the time 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import ToolSet.CostSeparation;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;


public class Balancer {
	private int partNum;
	private int capacity; // network capacity
	private int[] minReq; // length = the number of flows assigned to the network, thoughput_min_req of flows
	private int[] result; // how much capacity is allocated to each flow, length is the same to minReq.
	private int[] priority; // the priority of flows
	private int[] flowId; // each flow has it's unique identifier, which is different from array index.
	private int sumPrior;
	private double[] costPrior;

	private int max[]; // max_throughput
	private double cost;
	private double degree;
	private int step; // the step of decreasing resource
	private static boolean sc = false;
	private static int time = -1; // time slot
	private Config config;
	
	public Balancer(int capacity, int pN, int step, int sumP, Config configVar) {
		this.config = configVar;
		setPartNum(pN);
		setCapacity(capacity);
		minReq = new int[pN];
		costPrior = new double[pN];
		setMax(new int[pN]);
		setPriority(new int[pN]);
		setResult(new int[pN]);
		cost = 0;
		degree = 0.5;
		sumPrior = sumP;
		setStep(step);
	}

	public void allocate(int n, int t) {
		int rest = capacity - sum(minReq);
		result = minReq.clone();
		if (rest == 0) {
			return;
		}
		
		/****
		 * rest > 0 : the capacity of the network is larger than the sum_req of all flows
		 * strategy: satisfy the req of each flow (if it's not expensive), and then
		 * 			 allocate the rest resource according to priority (but now the priority is equal, each flow is with 1)
		 * similar to round robin
		 */
		if (rest > 0) {
			int waste = 0;

			for (int i = 0; i < result.length; i++) {

				int resultPrev = result[i];
				int totalPriority = sum(priority);
				if (totalPriority == 0) return;
				int flowId = getFlowId()[i];
				Flow flow = config.getFg().getFlows().get(flowId);
				double costUnsched = (double) flow.getImpUnsched()* flow.getImpUser();
				double costSched =  calcCost(flowId, t, n);
				double monetary = config.getNg().getNetworks().get(n).getCost() * config.getNg().getCostImportance();
				
				// if too expensive - the monetary cost too large
				if (monetary > costUnsched) {
					if (costUnsched != 0) {
						result[i] = (int) (result[i] / 100);//(monetary / costUnsched));
					} else {
						result[i] = result[i] / 100;
					}
				} 
				// Double.MAX_VALUE -> the flow-net mapping is not used by long-term scheduler
				if (costSched == Double.MAX_VALUE) {
					result[i] = 0;
					continue;
				}
				
				if (costSched > costUnsched*result[i]) {
					result[i] = 0;
					continue;
				} 
				result[i] =  Math.min(getMax()[i], result[i] + rest * priority[i] / totalPriority + waste);
				waste = resultPrev + rest * priority[i] / totalPriority + waste - getMax()[i];
				waste = waste > 0? waste : 0;			
			}
			return;
		}
		/***
		 * rest < 0 : capacity insufficient
		 * each flow has its resource_req, but not all of them can be satisfied.
		 * So we always choose the worst flow and cut down its request
		 */
		if (rest < 0) {
			rest = -rest;
			double[] curCost = new double[partNum];
			for (int i = 0; i < curCost.length; i++) {
				curCost[i] = calcCost(getFlowId()[i], t, n);
			}

			if (curCost.length == 0) return;
			// process those bad flow-net match - simpy assign with 0 and update the cost to the minimum
			// so that this flow is not involved in the allocation process any more
			for (int i = 0; i < curCost.length; i++) {
				if (curCost[i] == Double.MAX_VALUE) {
					int tmp = result[i];
					result[i] = 0;
					rest -= tmp;
					curCost[i] = -Double.MAX_VALUE;
				}
			}
			/****
			 * decreasing the req of worst flow part step by step
			 */
			for (int i = rest; i > 0; i -= getStep()) {
				int maxIndex = getMaxCostIndex(curCost);
				// pick up the worst flow
				if (result.length == 0 || (result[maxIndex] - getStep()) < 0) {
					// if the worst flow is the one with minimum rating value (best), then return
					if (curCost[maxIndex] == -Double.MAX_VALUE) { // .... a little silly...
						return;
					}
					// result[maxIndex] - getStep()) < 0 - the req of this flow cannot be decreased any more or it would be 0
					// here the step is one so if < 0, roll back
					i += getStep();
					curCost[maxIndex] = -Double.MAX_VALUE;
					continue;
				} else {
					if (curCost[maxIndex] == Double.MAX_VALUE) {
						int tmp = result[maxIndex];
						result[maxIndex] = 0;
						curCost[maxIndex] = -Double.MAX_VALUE;
					} else {
					result[maxIndex] -= getStep();
					curCost[maxIndex] -= config.getCs().getUnitstatefulReward(getFlowId()[maxIndex]);
					}
				}
			}

		}	
	}
	
	/***
	 * get the worst flow and decrease the resource for it.
	 * @param curCost
	 * @return
	 */
	public int getMaxCostIndex(double[] curCost) {
		double maxCost = -Double.MAX_VALUE;
//		////////System.out.println("partNum: " + partNum);
		int index = 0;
		for (int i = 0; i < partNum; i++) {	
			if (maxCost <= curCost[i]) {
				index = i;
				maxCost = curCost[i];
			} else if (maxCost == curCost[i]) {
				// if another flow cost is the same as the max, random pick one to maintain fairness
				if (index == -1) continue;
				if (priority[index] == priority[i]) {
					index = (new Random().nextInt(10) %2 == 0)? index: i;
				} else {
					index = (priority[index] < priority[i])? i: index;
				}
				
			}
		}
		return index;
	}
	/***
	 * this calcCost is used during allocation process
	 * the stateful cost is updated each round of allocation
	 * @param f
	 * @param t
	 * @param n
	 * @return
	 */
	public double calcCost(int f, int t, int n) {
		//int[] prevGlobal = CopyOfSimulation.getPrevious().getCombGlobal();
//		int[] prevGlobal = Simulation.getPrevious().getCombGlobal();

		int switchCost = 0;
		int monetaryCost = 0;
		sc = false; // take switch_cost into consideration or not
//		if (prevGlobal[f] != 0) {
//			if (prevGlobal[f] != (n + 1) && sc) {
//				switchCost = 2*1000;
//			}
//		}
		Flow flow = config.getFg().getFlows().get(f);
		Network net = config.getNg().getNetworks().get(n);
		monetaryCost = net.getCost() * config.getNg().getCostImportance();
		double rating = config.getCs().getNetworkMatch(f, n) + config.getCs().getStatelessReward(f)+	//stateless reward + network match
				config.getCs().getStatefulReward(f, t) * Math.max(1, flow.getTokensMin() / flow.getWindowMin()) +
				config.getCs().getTimeMatch(f, t) /getAvMinTp(config.getCs().getTg().getFlows().get(f)) + switchCost;
		// if the flow-net map is not used by long-term scheduler -> maximum value.
		if (config.getFlowNetFlag()[f][n] == 0) {
			rating = Double.MAX_VALUE;
			//System.out.println("**********small bad match: " + t + " - f" + f + " - n" + n + "rate" + rating);
		}
		return rating;
	}
	
	/**
	 *  calc the rating of each flow in this network
	 *  this cost function is used to rating the final allocation result, not involved in allocation process
	 * @param fId
	 * @param t
	 * @param n
	 * @return double - rating of allocation of each flow
	 */
	public double calcCostFinal(int fId, int t, int n) {
//		int[] prevGlobal = Simulation.getPrevious().getCombGlobal();
		int switchCost = 0;
//		int monetaryCost = 0;
		int f = getFlowId()[fId];

		Flow flow = config.getFg().getFlows().get(f);
		double rating = config.getCs().getNetworkMatch(f, n) + config.getCs().getStatelessReward(f)+	
				config.getCs().getStatefulReward(f, t) * result[fId] +
				config.getCs().getTimeMatch(f, t) /getAvMinTp(config.getCs().getTg().getFlows().get(f)) + switchCost;
		/**
		 *  naive implementation (take advantage of long-term scheduler)
		 *  netflowflag - records whether this flow-net mapping is used in long-term scheduler
		 *  smaller rating is better. - different process approaches for positive and negative values
		 */
		if (config.getFlowNetFlag()[f][n] == 0) {
			if (rating > 0) {
				rating = rating * 10000;
			} else {
				rating = Double.MAX_VALUE/ 2;
			}
		}
		return rating;
	}
	
	// get the whole rating of the concrete allocation plan (for network n, and its assigned flows)
	public double evaluator(int t, int n) {
		double sum = 0;
		// partNum -> how many flows assigned to this network
		for (int i = 0; i < partNum; i++) {
			int fId = getFlowId()[i];
			Flow flow = config.getFg().getFlows().get(fId);
			sum += (calcCostFinal(i , t, n) * result[i]); //+ monetaryCost*result[i] - (result[i] - (flow.getChunks() / (flow.getDeadline() - flow.getStartTime())))*costUnsched;
		}
		return sum;
	}
	
	protected int getAvMinTp(Flow flow){
		//get minimum throughput requirement of flow
		return (int) Math.ceil(flow.getTokensMin()/flow.getWindowMin())+1;
	}
	
	public void calPriorCost() {
		for (int i = 0; i < partNum; i++) {
			costPrior[i] = (double) priority[i] / sumPrior;
		}
	}
	
	
	public static int sum(int[] req) {
		int sum = 0;
		for (int i = 0; i < req.length; i++) {
			sum += req[i];
		}
		return sum;
	}

	
	public static void pick(int[] orig, int[] dest, int[] comb, int[] activeFlow, int nIndex) {
		// int[] result = new int[part[nIndex]];
		int rI = 0;
		if (dest.length == 0) return;

		for (int i = 0; i < comb.length; i++) {
			if (comb[i] == nIndex) {
				dest[rI] = orig[activeFlow[i]];
				rI++;
			}
		}
	}
	
	public static void pickFlowId(int[] dest, int[] comb, int[] activeFlow, int nIndex) {
		// int[] result = new int[part[nIndex]];
		int rI = 0;
		//////////System.out.println(nIndex);
		////Printer.printInt("orig", orig);
		////Printer.printInt("dest", dest);
		////Printer.printInt("comb", comb);
		if (dest.length == 0) return;

		for (int i = 0; i < comb.length; i++) {
			//////////System.out.println("i: " + i);
			//////////System.out.println("rI: " + rI);
			if (comb[i] == nIndex) {
				dest[rI] = activeFlow[i];
				////Printer.printInt("after dest", dest);

				rI++;
			}
		}
		////Printer.printInt("after dest", dest);
		// return result;
	}
	
	public static void pickFlowId(int[] dest, int[] comb,  int nIndex) {
		// int[] result = new int[part[nIndex]];
		int rI = 0;
		//////////System.out.println(nIndex);
		////Printer.printInt("orig", orig);
		////Printer.printInt("dest", dest);
		////Printer.printInt("comb", comb);
		if (dest.length == 0) return;

		for (int i = 0; i < comb.length; i++) {
			//////////System.out.println("i: " + i);
			//////////System.out.println("rI: " + rI);
			if (comb[i] == nIndex) {
				dest[rI] = i;
				////Printer.printInt("after dest", dest);

				rI++;
			}
		}
		////Printer.printInt("after dest", dest);
		// return result;
	}
	
	public static void pick(int[] orig, int[] dest, int[] comb, int nIndex) {
		// int[] result = new int[part[nIndex]];
		int rI = 0;
		// ////////System.out.println(nIndex);
		////Printer.printInt("orig", orig);
		////Printer.printInt("dest", dest);
		////Printer.printInt("comb", comb);
		if (dest.length == 0) return;

		for (int i = 0; i < comb.length; i++) {
		//	////////System.out.println("i: " + i);
		//	////////System.out.println("rI: " + rI);
			if (comb[i] == nIndex) {
				dest[rI] = orig[i];
				////Printer.printInt("after dest", dest);

				rI++;
			}
		}
		// //Printer.printInt("after dest", dest);
		// return result;
	}
	
	public static void pick(double[] orig, double[] dest, int[] comb, int nIndex) {
		int rI = 0;
		if (dest.length == 0) return;

		for (int i = 0; i < comb.length; i++) {
			if (comb[i] == nIndex) {
				dest[rI] = orig[i];
				rI++;
			}
		}
	}
	
	public static void pick(double[] orig, double[] dest, int[] comb, int[] activeFlow, int nIndex) {
		int rI = 0;
		if (dest.length == 0) return;
		for (int i = 0; i < comb.length; i++) {
			if (comb[i] == nIndex) {
				dest[rI] = orig[activeFlow[i]];
				rI++;
			}
		}
	}
	
	public int getPartNum() {
		return partNum;
	}
	public void setPartNum(int partNum) {
		this.partNum = partNum;
	}
	public int[] getMinReq() {
		return minReq;
	}
	public void setMinReq(int[] minReq) {
		this.minReq = minReq;
	}

	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getDegree() {
		return degree;
	}
	public void setDegree(double degree) {
		this.degree = degree;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int[] getResult() {
		return result;
	}

	public void setResult(int[] result) {
		this.result = result;
	}

	public int[] getPriority() {
		return priority;
	}

	public void setPriority(int[] priority) {
		this.priority = priority;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public double[] getCostPrior() {
		return costPrior;
	}

	public void setCostPrior(double[] costPrior) {
		this.costPrior = costPrior;
	}

	public int[] getMax() {
		return max;
	}

	public void setMax(int max[]) {
		this.max = max;
	}

	public int[] getFlowId() {
		return flowId;
	}

	public void setFlowId(int[] flowId) {
		this.flowId = flowId;
	}

	public static boolean isSc() {
		return sc;
	}

	public static void setSc(boolean sc) {
		Balancer.sc = sc;
	}
	
}
