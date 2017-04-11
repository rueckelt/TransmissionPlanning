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
import schedulingIOModel.UncertaintyErrorCalculation;
import ToolSet.CostSeparation;
import ToolSet.JsonLogger;
import ToolSet.LogMatlabFormat;
import adaptation.geneticAlgo.GeneticAlgo;
import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;

public class AdaptationScheduler extends HeuristicScheduler{

	private int[] mQ;
	private List<Integer> throughput; // size: flowSize
	private List<Integer> dataSize;   // size: flowSize
	private static Combination previous; //= new Combination();
	private Config config;
	
	//config parameters
	private boolean advancedInitialization=false;
	private boolean decide=true;	//use heuristic decision limit at all?
		
	
//	public AdaptationScheduler(NetworkGenerator ng, FlowGenerator tg) {
//		super(ng, tg);
//		// TODO Auto-generated constructor stub
//	}
//	
	public AdaptationScheduler(NetworkGenerator ng, FlowGenerator tg, FlowGenerator tgPred, NetworkGenerator ngPred, String logpath) {
		super(ng, tg);
		this.tgPred = tgPred;
//		System.out.println(logpath);
//		this.longTermSP = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logpath);
//		tg.setFlowIndices();
//		int i_n=0;
//		for(Network n:ng.getNetworks()){
//			n.setId(i_n);
//			i_n++;
//		}
		spLogPath=logpath;
	}

	private String name_extention="";

	@Override
	public String getType() {
		return "GA" + (advancedInitialization? "_init":"")+(isDecide()?"_bene":"")+getTypeExt();
	}

	
	@Override
	protected void calculateInstance_internal(String logfile) {
		super.calculateInstance_internal(logfile);	//initializes some stuff
		//reset indices of networks and flows. Required for GA.
		tg.setFlowIndices();
		int i_n=0;
		for(Network n:ng.getNetworks()){
			n.setId(i_n);
			i_n++;
		}
//		initCostSeparation();	//is done in Heuristic Scheduler
//		System.out.println(showSchedule(longTermSP_f_t_n));
		//adapt
		run(false, 1.0, true);
		
	}
	
	
	
	/**
	 * 
	 * @param dependence	shall only flows active in the long term schedule be considered?
	 * @param pctg			relative number of individuals in the population = #flows * pctg
	 * @param lookahead		time preserving strategy: allow flows to be allocated earlier than long term schedule says?
	 */
	public void run(boolean dependence, double pctg, boolean lookahead) {
//		System.out.println(showSchedule(longTermSP));
				
		initEnvConfig();
		
		int[][][] adapted = getEmptySchedule();//new int[tg.getFlows().size()][ng.getTimeslots()][ng.getNetworks().size()];
		
		
		// time slot loop
		for (int t = 0; t < ng.getTimeslots(); t++) {
//			System.out.println("AdaptationScheduler starting time slot t="+t);
			config.setTime(t);

			//***********************//
			updateFlowNetFlag(t, lookahead);	//mark which flow network combinations are allowed (preferred?)

			// each time slot - update initGene (intial solution from long-term schedule plan)
			config.clearIndividuals();
			
			config.addIndividual(extractIndividual(t));		//add the solution from the long term plan  
			
			
			if(advancedInitialization){
				config.addIndividual(previous.getComb());		//add the solution from the previous time step
				//add individual of the corresponding time slot of the long term plan without movement error
				int t_corr = getCorrespondingTimeSlot(t);
				if(t!=t_corr){
					config.addIndividual(extractIndividual(t_corr));
				}
			}
			
			updateInitSP(t); // TODO different strategy for initialization
			// each time slot - update network capacity
			updateNetworkConfig(true);
			updateNetCap(t);	//read out network capacities
			updateFlowAvl(t, dependence);	//read out flow availabilties
			updateMax();
			//**********************//
			/**********************************/
			GeneticAlgo ga = new GeneticAlgo();
			Individual result = ga.run(pctg, config); // 
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
//						cs.updateStatefulReward(f, netIndex-1, resource);
						
						//use the allocate method to update everything and check constraints.
						allocate(f, t, netIndex-1, resource);	
					}

				}
			}
			//update min-throughput request
			updateMQ(t);
			initDeactivate();
			
		}

//		if(!Arrays.deepEquals(adapted, getSchedule())){
//			System.out.println("AdaptationScheduler: result is not equal to allocated.");
//		}
//		setSchedule(adapted);
	}
	
	/**
	 * 
	 * @param t	current time slot after uncertainty
	 * @return the time slot of the original schedule, which corresponds to the current location of the vehicle
	 */
	private int getCorrespondingTimeSlot(int t) {
		int t0=0;
		while(t0<t && t0<ng.getTimeslots()){
			t0++;
		}
		return t0;	//failed
	}


	/**
	 * activate only those flows and networks for GA optimization which are active and beneficial
	 * @param t
	 */
	public void updateFlowNetFlag(int t){
		if(!decide){
			//use old model
			updateFlowNetFlag(t, true);
		}else{
			//use new model: only activate flows for which it is beneficial and tokens are released
			int t_additional_range=3;
			UncertaintyErrorCalculation unc = new UncertaintyErrorCalculation(tgPred.getFlows(), tg.getFlows(), ng.getTimeslots());

			for(int f=0; f<tg.getFlows().size();f++){
				if(unc.flowIsOrShouldBeActiveWithinTimeFrame(tg.getFlows().get(f).getId(), t-t_additional_range, t+t_additional_range)){
					for (int n = 0;n<ng.getNetworks().size(); n++){
						if(scheduleDecision(f, n, t)){
							config.getFlowNetFlag()[f][n]=1;
						}
					}
				}
			}
			
		}
	}
	//flags all network/flow matches that have been seen in the long-term plan.
	//This is a heuristic to preserve time selection
	//TODO: make it more efficient by keeping the state and updating just for current time slot? Use special case t=0 to init.
	public void updateFlowNetFlag(int t, boolean lookahead) {
				
		config.setFlowNetFlag(new int[tg.getFlows().size()][ng.getNetworks().size()]);		//set all flags to zero
		int range = ng.getTimeslots()/20;
		if (lookahead) {
			
			//if range covers all remaining time horizon, set all flags
			if (t + range >= ng.getTimeslots()) {
				for (int i = 0; i < config.getFlowNetFlag().length; i++) {
					Arrays.fill(config.getFlowNetFlag()[i], 1);
				}
				return;
			}
			
			
			//IF long term schedule has allocated tokens till now+range OR tokens of flow <10, THEN increase flag
			for (int i = 0; i < t + range; i++) {
				for (int f = 0; f < longTermSP_f_t_n.length && f < tg.getFlows().size(); f++) {
					for (int n = 0; n < longTermSP_f_t_n[0][0].length; n++) {
						if (longTermSP_f_t_n[f][i][n] != 0 || tg.getFlows().get(f).getTokensMin() < 10) {
							config.getFlowNetFlag()[f][n] += 1;		//why +=1 and not =1?
						}
					}
				}
			}
			
			//also activate flags for all NEW flows that are not in the long term plan
			if (tg.getFlows().size() > longTermSP_f_t_n.length) {
				for (int f = longTermSP_f_t_n.length; f < tg.getFlows().size(); f++) {
					for (int n = 0; n < longTermSP_f_t_n[0][0].length; n++) {
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
	
	//get initial individual (networks) from long term SP timeslot t
	public void updateInitSP(int ts) {
		if (ts >= longTermSP_f_t_n[0].length) return;
		for (int f = 0; f < longTermSP_f_t_n.length && f < config.getInitGenes().length; f++) {
			for (int n = 0; n < longTermSP_f_t_n[0][0].length; n++) {
				if (longTermSP_f_t_n[f][ts][n] != 0) {
				//	////////System.out.println(config.getInitGenes().length);
				//	//Printer.printInt(config.getInitGenes());
//					System.out.println("Init gene f="+f+", net="+n+" in t="+ts);
					config.getInitGenes()[f] = n + 1;	//assign network id to each flow
				}
			}
		}
		
	}
	
	public int[] extractIndividual(int t){
		int[] geneSeq = new int[longTermSP_f_t_n.length];	// geneSeq[flow]=assigned network
		if (t >= longTermSP_f_t_n[0].length) return null;
		for (int f = 0; f < longTermSP_f_t_n.length && f < config.getInitGenes().length; f++) {
			for (int n = 0; n < longTermSP_f_t_n[0][0].length; n++) {
				if (longTermSP_f_t_n[f][t][n] != 0) {
					geneSeq[f] = n + 1;	//assign network id to each flow
				}
			}
		}
		return geneSeq;
	}
	
	public void updateNetworkConfig(boolean onOff) {
		for (Network n : ng.getNetworks()) {
			activateNetwork(n.getId(), onOff);
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
			}
			else{
				activateNetwork(n.getId(), true);
			}
//			System.out.println("net="+n.getId()+", cap="+cap+", in ts="+ts);
		}
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
	
	
	//INITIALIZATION
	
	public void initEnvConfig() {
		// f - minReq
		config = new Config(ng, tg);
//		CostSeparation cs = new CostSeparation(tg, ng);	//done in heuristicScheduler
		config.setCs(cs);
		//Simulation.setPrevious(new Combination());
		config.setAdaptScheduler(this);

		for (Flow f : tg.getFlows()) {
			////////System.out.println("id: " + f.getIndex() + " - " + f.getTokensMin() + " - " + f.getWindowMin());
			// TODO 
			config.getmQ()[f.getIndex()] = (int) f.getTokensMin() / f.getWindowMin();
			mQ = config.getmQ().clone();
		}
		
		// f - priority
		for (Flow f : tg.getFlows()) {
			////////System.out.println("id: " + f.getIndex() + " - " + f.getImpUser());
			config.getPrior()[f.getIndex()] = 1; //do not use this
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
	
//	public void updateFlowConfig(boolean onOff) {
//		for (Flow f : tg.getFlows()) {
//			activateFlow(f.getIndex(), onOff);
//		}
//	}

	
	public void activateFlow(int index, boolean active) {
		config.getActiveFlowBool()[index] = active? 1 : 0;
	}
	
	public void activateNetwork(int index, boolean active) {
//		System.out.println("activateNet: "+index);
		config.getActiveNetworkBool()[index] = active? 1 : 0;
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
	
	
	public List<Integer> getThroughput() {
		return throughput;
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
	

	public Combination getPrevious() {
		return previous;
	}

	public void setPrevious(Combination prev) {
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

	public void setLocationCorrection(boolean locationCorrection) {
		this.advancedInitialization = locationCorrection;
	}


	public boolean isDecide() {
		return decide;
	}


	public HeuristicScheduler setDecide(boolean decide) {
		this.decide = decide;
		return this;
	}

}
