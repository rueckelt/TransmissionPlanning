package adaptation.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import schedulers.AdaptationScheduler;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;
import ToolSet.CostSeparation;

public class Config {
	/*** The enviornment config - flow, net and costSeparation, initial solution and so on ***/
	private   int netNum = 5, flowNum = 10;
	// the initial values does not make sense...they will be overwriten later
	private   int[] mQ = {65, 20, 50, 30, 80, 50, 0}; // min_req of each flow
	private   int[] max = {100, 100, 100, 40, 120, 100, 0}; // max_req of each flow
	private   int[] prior = {3, 3, 2 ,2, 1, 3, 0}; // priority of each flow
	private int[] capReal;
	private   int priorSum = sum(getPrior());
	private   int[]initGenes = {1, 2, 3, 2, 1, 0, 0, 0, 0, 0};
	private   int round = 100;
	private   int[] activeNetworkBool = {1, 1, 1, 1, 0, 0, 0};
	private   int[] networkType = {1, 2, 2, 1, 1, 2, 2};
	private    HashMap<Integer, Set<Integer>> unsedFNMap;
	private    int[][] flowNetFlag;
	private    int[] networkFlag = {0, 0, 0, 0, 0};
	private    NetworkGenerator ng;
	private    FlowGenerator fg;
	private 	AdaptationScheduler scheduler;

	private    int[] activeFlowBool = {1, 1, 1, 1, 1, 0, 0, 0}; // 8
	private    int time;
	private    CostSeparation cs;
	
	private		Vector<int[]> myIndividuals = new Vector<int[]>();
	
	/*** Config config ends ***/
	
	public Config(NetworkGenerator ng, FlowGenerator tg) {
		setNg(ng);
		setFg(tg);
		setmQ(new int[tg.getFlows().size()]);
		setMax(new int[tg.getFlows().size()]);
		setActiveFlowBool(new int[tg.getFlows().size()]);
		setPrior(new int[tg.getFlows().size()]);
		setActiveNetworkBool(new int[ng.getNetworks().size()]);
		setNetworkType(new int[ng.getNetworks().size()]);
		setNetworkFlag(new int[ng.getNetworks().size()]);
		setCapReal(new int[ng.getNetworks().size()]);
		setNetNum(ng.getNetworks().size());
		setFlowNum(tg.getFlows().size());
		setUnsedFNMap(new HashMap<Integer, Set<Integer>>());

	}

	public    int sum(int[] req) {
		int sum = 0;
		for (int i = 0; i < req.length; i++) {
			sum += req[i];
		}
		return sum;
	}
	
	public    int getFlowNum() {
		return mQ.length; //flowNum;
	}

	public    void setFlowNum(int flowNumVar) {
		flowNum = flowNumVar;
	}

	public   int getNetNum() {
		return netNum;
	}

	public    void setNetNum(int netNumVar) {
		netNum = netNumVar;
	}


	public    int[] getInitGenes() {
		return initGenes;
	}
	
	public void clearIndividuals(){
		myIndividuals.clear();
	}
	
	public void addIndividual(int[] geneSeq){
		myIndividuals.add(geneSeq);
	}

	public   void setInitGenes(int[] initGenesVar) {
		initGenes = initGenesVar;
	}

	public   int getRound() {
		return round;
	}

	public   void setRound(int roundVar) {
		round = roundVar;
	}
	
	public   CostSeparation getCs() {
		return cs;
	}



	public    void setCs(CostSeparation csVar) {
		cs = csVar;
	}



	public   int getTime() {
		return time;
	}



	public    void setTime(int timeVar) {
		time = timeVar;
	}



	public    int[] getNetworkType() {
		return networkType;
	}
	public    void extend(int[] orig, int[] sub, int[] index) {
		for (int i = 0; i < sub.length; i++) {
			orig[index[i]] = sub[i];
		}
	}
	
	/**
	 * 
	 * @return array over active flows, containing the original index of all flows
	 */
	public   int[] getActiveFlow() {
		int[] activeFlow = new int[getActiveFlowNum()];
		for (int i = 0, aF = 0; i < getFlowNum(); i++) {
			if (getActiveFlowBool()[i] == 1) {
				activeFlow[aF] = i;
				aF++;
			}
		}
		//////////System.out.println("active Flow: ");
		////Printer.printInt(activeFlow);
		return activeFlow;
	}
	
	public    int getActiveFlowNum() {
		int result = 0;
		for (int af: getActiveFlowBool()) {
			if (af > 0) {
				result++;
			}
		}
		return result;
	}
	
	public   int getActiveNetNum() {
		int result = 0;
		for (int af: getActiveNetworkBool()) {
			if (af > 0) {
				result++;
			}
		}
		return result;
	}
	
	public   Vector<Integer> getAvailableNetworks() {
		Vector<Integer> netPool = new Vector<Integer>();
		//netPool.add(0);
		for (int i = 0; i < getActiveNetworkBool().length; i++) {
			if(getActiveNetworkBool()[i] > 0) {
				netPool.add(i+1);
			}
		}
		return netPool;
	}
	
	public   HashMap<Integer, Set<Integer>> getNetTypeMap() {
		HashMap<Integer, Set<Integer>> result = new HashMap();
		int[] activeNet = getActiveNetworkBool();
		int[] netType = getNetworkType();
		for (int i = 0; i < getActiveNetworkBool().length && i < netType.length; i++) {
			if (activeNet[i] > 0) {
				if (result.containsKey(netType[i])) {
					result.get(netType[i]).add(i + 1);
				} else {
					Set<Integer> arr = new HashSet<Integer>();
					arr.add(i + 1);
					result.put(netType[i], arr);
				}
			}
		}
		return result;
	}






	public   void setPriorSum(int priorSum) {
		priorSum = priorSum;
	}



//	public    int[] getCapReal() {
//		return capReal;
//	}
//
//
//
//	public    void setCapReal(int[] capRealVar) {
//		capReal = capRealVar;
//	}

	public    void setNetworkType(int[] networkTypeVar) {
		networkType = networkTypeVar;
	}
		public   NetworkGenerator getNg() {
			return ng;
		}



		public    void setNg(NetworkGenerator ngVar) {
			ng = ngVar;
		}



		public    FlowGenerator getFg() {
			return fg;
		}



		public    void setFg(FlowGenerator fgVar) {
			fg = fgVar;
		}



		public  int[] getNetworkFlag() {
			return networkFlag;
		}



		public    void setNetworkFlag(int[] networkFlagVar) {
			networkFlag = networkFlagVar;
		}



		public  HashMap<Integer, Set<Integer>> getUnsedFNMap() {
			return unsedFNMap;
		}



		public    void setUnsedFNMap(HashMap<Integer, Set<Integer>> unsedFNMapVar) {
			unsedFNMap = unsedFNMapVar;
		}



		public    int[][] getFlowNetFlag() {
			return flowNetFlag;
		}



		public    void setFlowNetFlag(int[][] flowNetFlagVar) {
			flowNetFlag = flowNetFlagVar;
		}
		
		/*
		 * 
		 */
		public   int[] getmQ() {
			return mQ;
		}


	 
		public   void setmQ(int[] mQVar) {
			mQ = mQVar;
		}


		public    int[] getPrior() {
			return prior;
		}



		public    void setPrior(int[] priorVar) {
			prior = priorVar;
		}



		public   int[] getActiveNetworkBool() {
			return activeNetworkBool;
		}



		public   void setActiveNetworkBool(int[] activeNetworkBoolVar) {
			activeNetworkBool = activeNetworkBoolVar;
		}



		public   int[] getActiveFlowBool() {
			return activeFlowBool;
		}



		public   void setActiveFlowBool(int[] activeFlowBoolVar) {
			activeFlowBool = activeFlowBoolVar;
		}

		public   int getPriorSum() {
			return priorSum;
		}
		

		public   int[] getMax() {
			return max;
		}

		public   void setMax(int[] maxVar) {
			max = maxVar;
		}

		public int[] getCapReal() {
			// TODO Auto-generated method stub
			return capReal;
		}

		public void setCapReal(int[] is) {
			// TODO Auto-generated method stub
			this.capReal = is;
		}

		public AdaptationScheduler getAdaptScheduler() {
			return scheduler;
		}

		public void setAdaptScheduler(AdaptationScheduler scheduler) {
			this.scheduler = scheduler;
		}

	
}
