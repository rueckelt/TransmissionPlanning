package schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import ToolSet.CostSeparation;
import ToolSet.JsonLogger;
import ToolSet.LogMatlabFormat;
import adaptation.geneticAlgo.CCP_Ga;
import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;

public class AdaptationScheduler extends Scheduler{
	private int[][][] longTermSP;
	private String spLogPath;
	private int[] mQ;
	private List<Integer> throughput; // size: flowSize
	private List<Integer> dataSize;   // size: flowSize
	private static Combination previous; //= new Combination();
	private FlowGenerator tgPred;
	private Config config;
	public AdaptationScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
		// TODO Auto-generated constructor stub
	}
	
	public AdaptationScheduler(NetworkGenerator ng, FlowGenerator tg, FlowGenerator tgPred, String logpath) {
		super(ng, tg);
		this.tgPred = tgPred;
		spLogPath=logpath;
	}
	public AdaptationScheduler(NetworkGenerator ng, FlowGenerator tg, String logpath) {
		super(ng, tg);
		this.longTermSP = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logpath);
		System.out.println(showSchedule(longTermSP));
		//		this.longTermSP = JsonLogger.json2Array(logpath);
	}

	public void loadLongTermSp(String logfile){
		this.longTermSP = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logfile);
	}
	
	public void run(boolean dependence, double pctg, boolean lookahead) {
//		System.out.println(showSchedule(longTermSP));
		tg.setFlowIndices();
		initEnvConfig();
		
		int[][][] adapted = getEmptySchedule();//new int[tg.getFlows().size()][ng.getTimeslots()][ng.getNetworks().size()];

		// time slot loop
		for (int t = 0; t < ng.getTimeslots(); t++) {
			config.setTime(t);

			//***********************//
			updateFlowNetFlag(t, lookahead);

			// each time slot - update initGene (intial solution from long-term schedule plan)
			updateInitSP(t); // TODO different strategy for initialization
			// each time slot - update network capacity
			updateNetworkConfig(true);
			updateNetCap(t);	//read out network capacities
			updateFlowAvl(t, dependence);	//read out flow availabilties
			updateMax();
			//**********************//
			/**********************************/
			Individual result = CCP_Ga.run(pctg, config); // 
			/**********************************/

			setPrevious(result.getComb());

			// result copy -> 3d int array
			for (int f = 0; f < tg.getFlows().size(); f++) {
				int netIndex = result.getComb().getCombGlobal()[f];
				if (netIndex > 0) {
					int resource = result.getComb().getResultGlobal()[f];
					if (resource > config.getCapReal()[netIndex - 1]) {
						//continue;
					} else {
						adapted[f][t][netIndex - 1] = resource;
						getThroughput().set(f, getThroughput().get(f) + resource);
					}

				}
			}
			//update min-throughput request
			updateMQ(t);
			initDeactivate();
			
		}

		setSchedule(adapted);
	}
	
	public void initEnvConfig() {
		// f - minReq
		config = new Config(ng, tg);
		CostSeparation cs = new CostSeparation(tg, ng);
		config.setCs(cs);
		//Simulation.setPrevious(new Combination());


		for (Flow f : tg.getFlows()) {
			////////System.out.println("id: " + f.getIndex() + " - " + f.getTokensMin() + " - " + f.getWindowMin());
			// TODO 
			config.getmQ()[f.getIndex()] = (int) f.getTokensMin() / f.getWindowMin();
			mQ = config.getmQ().clone();
		}
		
		// f - priority
		for (Flow f : tg.getFlows()) {
			////////System.out.println("id: " + f.getIndex() + " - " + f.getImpUser());
			// TODO 
			config.getPrior()[f.getIndex()] = 1; //f.getImpUser();
		}
		
		setDataSize(new ArrayList<Integer>());
		setThroughput(new ArrayList<Integer>());

		for (Flow f : tg.getFlows()) {
			getDataSize().add(f.getTokens());
			getThroughput().add(0);
			config.getMax()[f.getIndex()] = Math.min(f.getTokensMax() / f.getWindowMax(), f.getTokens());
		}
		
		for (int i = 0; i < ng.getNetworks().size(); i++) {
			config.getNetworkType()[i] = ng.getNetworks().get(i).getType();
//			//Printer.printInt("net type after initialization: ", config.getNetworkType());
		}	
		initDeactivate();
		updateNetworkConfig(true);
	}
	
	public void initDeactivate() {
		for (int i = 0; i < config.getActiveFlowBool().length; i++) {
			config.getActiveFlowBool()[i] = 0;
		}
		
		for (int i = 0; i < config.getActiveNetworkBool().length; i++) {
			config.getActiveNetworkBool()[i] = 0;
		}
		
		for (int i = 0; i < config.getInitGenes().length; i++) {
			config.getInitGenes()[i] = 0;
		}
		
		for (int i = 0; i < config.getCapReal().length; i++) {
			config.getCapReal()[i] = 0;
		}
	}
	
	public void updateFlowConfig(boolean onOff) {
		for (Flow f : tg.getFlows()) {
			activateFlow(f.getIndex(), onOff);
		}
	}
	
	public void updateNetworkConfig(boolean onOff) {
		for (Network n : ng.getNetworks()) {
			activateNetwork(n.getId(), onOff);
		}
	}
	
	public void activateFlow(int index, boolean active) {
		config.getActiveFlowBool()[index] = active? 1 : 0;
	}
	
	public void activateNetwork(int index, boolean active) {
//		System.out.println("activateNet: "+index);
		config.getActiveNetworkBool()[index] = active? 1 : 0;
	}
	
	//get initial individual (networks) from long term SP timeslot t
	public void updateInitSP(int ts) {
		if (ts >= longTermSP[0].length) return;
		for (int f = 0; f < longTermSP.length && f < config.getInitGenes().length; f++) {
			for (int n = 0; n < longTermSP[0][0].length; n++) {
				if (longTermSP[f][ts][n] != 0) {
				//	////////System.out.println(config.getInitGenes().length);
				//	//Printer.printInt(config.getInitGenes());
//					System.out.println("Init gene f="+f+", net="+n+" in t="+ts);
					config.getInitGenes()[f] = n + 1;	//assign network id to each flow
				}
			}
		}
		
	}
	
	//TODO combine with network availability
	public void updateNetCap(int ts) {
		for (Network n : ng.getNetworks()) {
			//////////System.out.println("newtork start time: " + n.getId() + " - " + n.getSlots() + " / " + n.);
			int cap = n.getCapacity().get(ts);	
			config.getCapReal()[n.getId()] = cap;
			if (cap == 0) {
				activateNetwork(n.getId(), false);
			}else{
				activateNetwork(n.getId(), true);
			}
//			System.out.println("net="+n.getId()+", cap="+cap+", in ts="+ts);
		}
	}
	
	public void updateFlowAvl(int ts) {
		for (Flow f : tg.getFlows()) {
	//		////////System.out.println("f-id: " + f.getId() + " - " + f.getStartTime());
			if (f.getStartTime() <= ts) {
				activateFlow(f.getIndex(), true);
			}
			int tp = getThroughput().get(f.getIndex());
			int ds = getDataSize().get(f.getIndex());
			if (ts > f.getDeadline() && f.getIndex() % 4 != 1) {
				activateFlow(f.getIndex(), false);	
			}
			if (tp > 0 && tp >= ds) {
				activateFlow(f.getIndex(), false);
			}
		}
//		//Printer.printInt("activeFlow: ", comb.getActiveFlowBool());		
	}
	
	public void updateFlowAvl(int ts, boolean useOnlyScheduledFlow) {

		for (Flow f : tg.getFlows()) {
	//		////////System.out.println("f-id: " + f.getId() + " - " + f.getStartTime());
			if (f.getStartTime() <= ts) {
				activateFlow(f.getIndex(), true);
//				System.out.println("flow active f="+f.getIndex()+", ts="+ts);
			}
			int tp = getThroughput().get(f.getIndex());
			int ds = getDataSize().get(f.getIndex());
			if (ts > f.getDeadline()) {// && !f.isBufferable()) {
				activateFlow(f.getIndex(), false);	
//				System.out.println("flow inactive f="+f.getIndex()+", ts="+ts+"  deadline exceeded");
			}
			if (tp > 0 && tp >= ds) { 
				activateFlow(f.getIndex(), false);
//				System.out.println("flow inactive f="+f.getIndex()+", ts="+ts+"  throughput");
			}
		}
		if (useOnlyScheduledFlow) {
			for (int i = 0; i < config.getInitGenes().length && i < tg.getFlows().size(); i++) {
				if (config.getInitGenes()[i] == 0) {
					activateFlow(i, false);
				}
			}
			
			Set<Integer> newflows = getNewflowsId();
			////////System.out.println("newflows: " + newflows.toString());
			for (Integer nf : newflows) {
				activateFlow(nf, true);
				Flow f = tg.getFlows().get(nf);
				if (f.getStartTime() > ts) {
					activateFlow(f.getIndex(), false);
				}
				int tp = getThroughput().get(f.getIndex());
				int ds = getDataSize().get(f.getIndex());
				if (tp > 0 && tp >= ds) {
					activateFlow(f.getIndex(), false);
				}

			}
		}
//		//Printer.printInt("activeFlow: ", comb.getActiveFlowBool());
		
	}
	
	public void updateMax() {
		int fId = 0;
		int maxPrev = 0;
		for (Flow f : tg.getFlows()) {
			fId = f.getIndex();
			maxPrev = config.getMax()[fId];
			config.getMax()[fId] = Math.min(getDataSize().get(fId) - getThroughput().get(fId), maxPrev);
		}
	}
	
	public void updateMQ(int t) {
		int fId = 0;
		int mqPrev = 0;
		for (Flow f : tg.getFlows()) {
			fId = f.getIndex();
			mqPrev = config.getmQ()[fId];
			config.getmQ()[fId] = Math.min(getDataSize().get(fId) - getThroughput().get(fId), mqPrev);
			if (f.getDeadline() < t) { // && f.isBufferable() == false) {
				config.getmQ()[fId] = 0;
			}
			//////System.out.println("f_t: " + f.getId()+"_"+t + "rest: " + (getDataSize().get(fId) - getThroughput().get(fId)) + "-mq: " + comb.getmQ()[fId]);

		}
	}
	
	public void updateFlowNetFlag(int t, boolean lookahead) {
		config.setFlowNetFlag(new int[tg.getFlows().size()][ng.getNetworks().size()]);
		int range = 15;
		if (lookahead) {
			if (t + range >= ng.getTimeslots()) {
				for (int i = 0; i < config.getFlowNetFlag().length; i++) {
					Arrays.fill(config.getFlowNetFlag()[i], 1);
				}
				return;
			}
			for (int i = 0; i < t + range; i++) {
				for (int f = 0; f < longTermSP.length && f < tg.getFlows().size(); f++) {
					for (int n = 0; n < longTermSP[0][0].length; n++) {
						if (longTermSP[f][i][n] != 0 || tg.getFlows().get(f).getTokensMin() < 10) {
							config.getFlowNetFlag()[f][n] += 1;
						}
					}
				}
			}
			if (tg.getFlows().size() > longTermSP.length) {
				for (int f = longTermSP.length; f < tg.getFlows().size(); f++) {
					for (int n = 0; n < longTermSP[0][0].length; n++) {
						config.getFlowNetFlag()[f][n] += 1;				
					}
				}	
			}
		} else {
			for (int i = 0; i < config.getFlowNetFlag().length; i++) {
				Arrays.fill(config.getFlowNetFlag()[i], 1);
			}
			return;			
		}
	}
	
	public List<Integer> getThroughput() {
		return throughput;
	}
	
	@Override
	protected void calculateInstance_internal(String logfile) {
		//load transmission plan from log file
		this.longTermSP = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", spLogPath);
		//adapt
		run(false, 1.0, true);
		
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "GA";
	}

	public List<Integer> getDataSize() {
		return dataSize;
	}

	public void setDataSize(List<Integer> dataSize) {
		this.dataSize = dataSize;
	}

	public void setThroughput(List<Integer> throughput) {
		this.throughput = throughput;
	}
	

	public static Combination getPrevious() {
		return previous;
	}

	public static void setPrevious(Combination prev) {
		previous = prev;
	} 
	
	public Set<Integer> getNewflowsId() {
		Set<Integer> nf = new HashSet<Integer>();
		FlowGenerator ng1 = new FlowGenerator();
		FlowGenerator ng2 = new FlowGenerator();
		if (tgPred.getFlows().size() > tg.getFlows().size()) {
			ng1 = tgPred;
			ng2 = tg;
			return nf;
		} else {
			ng1 = tg;
			ng2 = tgPred;
		}
		
		int sizeLong = ng1.getFlows().size();
		int sizeShort = ng2.getFlows().size();
		
		for (int i = sizeShort; i < sizeLong; i++) {
			//TODO this is for exector adding new flows
			////////System.out.println("fid: " + ng1.getFlows().get(i).getIndex());
			
			nf.add(ng1.getFlows().get(i).getIndex());
		}
		return nf;
	}
	
//	public static void main(String[] args) {
//		/*****************Init Simulation Parameters***************/
//		ArrayList<String> verf_true = new ArrayList<String>();
//		ArrayList<String> verf_false = new ArrayList<String>();
//
//		boolean dependence = false; 
//		boolean plotBool = true;
//		boolean others = true;
//		boolean lookahead = false;
//		boolean logConv = false;
//		float strength = (float) 0; //5.0 yes, 10.0 - no
//		float flowStrength = 0.0f;
//		float moveStrength = 0.0f;
//		float netStrength = 0.3f;
//		boolean overwrite = false;
//		float offset = (float) 0;
//		int timesteps = 5;
//		int flowNum = 8;
//		int netNum = 8;
//		boolean root = false;
//		int simTime = 100;
//		double pctg = 1.0; // the factor for population size
//		//String folder_out_root = "NewConfig" + flowNum + "_n" + netNum + "_t" + simTime + File.separator + "test" + File.separator + strength + "_" + offset   + File.separator;// stren_add + "_" + stren_cont + "_" + timesteps + File.separator;
//		String folder_out_root = "my_logs/" + File.separator + "eval_uncertainty" + File.separator + "3_2_3" + File.separator;
//		String log_name = "";
//		int group = 1; // the number of repetitions
//		/**********************************************************/
//		for (int r = 0; r < group; r++) {  // reptition
//			CCP_Ga.setConvergeList(new ArrayList<ArrayList<Double>>());
//
//			String folder_out = folder_out_root + r + File.separator;
//			new File(folder_out).mkdir();
//			log_name = "";
//
//			/******************Network Flow Initial********************/
//			NetworkGenerator ngClone = EvaluationScenarioCreator.getNetworkGeneratorRoot(folder_out);
//			FlowGenerator tgClone = EvaluationScenarioCreator.getFlowGeneratorRoot(folder_out);
//			for (Network n : ngClone.getNetworks()) {
//				System.out.println(n.toString());
//			}
//			for (Flow flow : tgClone.getFlows()) {
//				System.out.println(flow.toString());
//			}
//			// Generate Original Longterm scheduler
//			Test.testLongTerm(true, tgClone, ngClone, plotBool, "long_term_without_uncertainty", "");
//			/***********************Uncertainty************************/
//			NetworkGenerator ngReal;
//			FlowGenerator tg;
//			String folder_out_uncert;
//			if (root) {
//				ngReal = EvaluationScenarioCreator.getNetworkGeneratorRoot(folder_out);//EvaluationScenarioCreator.getNetworkGenerator(folder_out, overwrite, netNum, simTime, netStrength, moveStrength);
//				tg = EvaluationScenarioCreator.getFlowGeneratorRoot(folder_out);//EvaluationScenarioCreator.getFlowGenerator(folder_out, overwrite, flowNum, simTime, flowStrength);
//				folder_out_uncert = folder_out;
//			} else {
//				ngReal = EvaluationScenarioCreator.getNetworkGenerator(folder_out, overwrite, netNum, simTime, netStrength, moveStrength);
//				tg = EvaluationScenarioCreator.getFlowGenerator(folder_out, overwrite, flowNum, simTime, flowStrength);
//				folder_out_uncert = EvaluationScenarioCreator.getFilePath(folder_out, netStrength, moveStrength, flowStrength);
//			}
//			for (Network n : ngReal.getNetworks()) {
//				System.out.println(n.toString());
//			}
//			for (Flow flow : tg.getFlows()) {
//				System.out.println(flow.toString());
//			}
//			// init logger
//			LogMatlabFormat log = new LogMatlabFormat();
//			CostFunction cf = new CostFunction(ngReal, tg, log);
//			String pathLTS = "long_term_without_uncertainty0"; // this is simply a path to store the long term scheduler in json
//			
//			System.out.println("******************true********************");
//			
//			/*********adaptation + true = what we used ***********************/
//			LogMatlabFormat log_atrue = new LogMatlabFormat();
//			CostFunction cf_atrue = new CostFunction(ngReal, tg, log_atrue);
//			AdaptationScheduler adaptScheduler = new AdaptationScheduler(ngReal, tg, pathLTS);
//			//Simulation simTrue = new Simulation(pathLTS, tg, ngReal);
//			//simTrue.simTime = simTime;
//			dependence = false;
//			lookahead = true;
//			adaptScheduler.run(dependence, pctg, lookahead);
//			if (plotBool) {
//				Plot plot2 = new Plot(new VisualizationPack(ngReal, tg, adaptScheduler.getSchedule()));
//			}
//		}
//	}

}
