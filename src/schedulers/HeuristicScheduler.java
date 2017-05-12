package schedulers;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.UncertaintyErrorCalculation;
import ToolSet.CostSeparation;
import ToolSet.LogMatlabFormat;


public abstract class HeuristicScheduler extends Scheduler{
	
	
	//for normal heuristics
	public HeuristicScheduler(NetworkGenerator ng, FlowGenerator tg) {
		
		super(ng, tg);
		if(ng!=null&&tg!=null){
			ng_tmp=ng.clone();		//make a copy of ng to be able to change it
		}
	}
	
	//For adaptation
	public HeuristicScheduler(NetworkGenerator ng, FlowGenerator tg, NetworkGenerator ngPred, FlowGenerator tgPred, String longTermSchedule_logfile){
		super(ng, tg);
		this.tgPred=tgPred;
		this.ngPred=ngPred;
		spLogPath=longTermSchedule_logfile;
	}
	
	public HeuristicScheduler(NetworkGenerator ng, FlowGenerator tg, NetworkGenerator ngPred, 
			FlowGenerator tgPred, String longTermSchedule_logfile, String type_ext){
		this(ng,tg,ngPred,tgPred,longTermSchedule_logfile);
		this.typeExt=type_ext;
	}
		
	
	
	protected NetworkGenerator ng_tmp; //remove scheduled chunks from this ng
	protected int schedule_decision_limit =0;		//clim cost limit. positive fosters allocation. negative impairs allocation.
	protected int tl_offset =0;	//allowed time offset for which violation is allowed
	
	protected boolean NEW_RATING_ESTIMATOR=true;
	protected boolean ADAPTIVE_loc = false;
	protected boolean ADAPTIVE_err = false;
	protected boolean ADAPTIVE_transm = false;
	protected boolean ADAPTIVE_add = false;
//	protected final boolean ADAPTIVE = false;
	protected int WINDOW = -1;
	//-1 is automatic from smape. Else vary between 0 and 1; 1 means full impairing = follow plan!
	protected double TIME_IMPAIRING_WEIGHT = 0;	
	protected double NET_IMPAIRING_WEIGHT = 0;	
	protected CostSeparation cs;
	protected String spLogPath;

	//for adaptation heuristics
	protected int[][][] longTermSP_f_t_n;	
	protected FlowGenerator tgPred;
	protected NetworkGenerator ngPred;
	private int[][] prefixSumLongTermSP_fid_t;
	private int dropped_f[];
	private int additionallyAllocated_f_t[][];
	protected String typeExt = "";
	
	@Override
	protected void calculateInstance_internal(String logfile) {
		if(spLogPath!=null){
			loadLongTermSp(spLogPath);
		}
		if(NEW_RATING_ESTIMATOR) initCostSeparation();
	}
	
	
	/**
	 * activates new rating scheme for cost estimation
	 * @param NEW_RATING
	 * @return
	 */
	public HeuristicScheduler newRating(boolean NEW_RATING){
		NEW_RATING_ESTIMATOR=NEW_RATING;
		tl_offset=0;	//allow the new metric to violoate time limit constraints
		return this;
	}
	
	protected void initCostSeparation(){
		cs= new CostSeparation(tg, ng);
	}
	protected CostSeparation getCostSeparation(){
		return cs;
	}
	
	public boolean scheduleDecision(int f, int n, int t) {
		return oppScheduleDecision(f, n, t);
	}
	
	public Scheduler setScheduleDecisionLimit(int limit){
		schedule_decision_limit=limit;
		return this;
	}
	public int getScheduleDecisionLimit(){
		return schedule_decision_limit;
	}
	
	
	
	/**
	 * 
	 * covers the heuristics for time impairing to follow long term plans.
	 * must be minus max to impair most.
	 * @param f
	 * @param t
	 * @return limit for opportunistic scheduling
	 */
	public int getScheduleDecisionLimit(int f, int t){
		
		//when there is a long term plan to follow
		if(longTermSP_f_t_n!=null){
			//initialize prefix sum if not done yet
			if(prefixSumLongTermSP_fid_t == null){
				boolean success = initializePrefixSum();
				if(!success) return 0;
			}
			
			
			int ref_t = t;
			//heuristic to look up at another point in time, which corresponds to the vehicle location
			if(ADAPTIVE_loc){
				ref_t = getRefSlot(t, ng.getSlotChange(), f);
			}

			double h=0;	//no impairing
			//impair if allocated equal or more than planned (to allocate)
			//and no transmission in reference slot
//			if(too_many_allocated_tokens>=0 && !transmission_in_slot){
			if(!moreTokensReleasedThanAllocated(f, t) && !transmissionInSlot(f, t)){
				double err = 0;
				if(ADAPTIVE_err){
					err=Math.min(1,getError(f, ref_t));
				}

				int attr_force = cs.getStatefulReward(f, t)+cs.getStatelessReward(f);
				h= (1-err) * attr_force;		
			}
			
//			if(f==0){
//				System.out.println("Heuristic: getScheduleDecisionOpp: " +
//					"f="+f+"("+f_id+"), t="+t+", ref_t="+ref_t+", too many alloc: "+too_many_allocated_tokens+", h="+h);
//			}
			return (int)h;

		}
		return 0;
	}
	
//	protected int getTokensMaxTp(int f, int t){
//		if(longTermSP_f_t_n!=null){
//			if(!transmissionInSlot(f, t)){
//				//during opportunistic allocation, release only exactly as many as released from function. 
//				return Math.min(tg.getFlows().get(f).getTokensMax(), //upper limit is TokensMax
//						Math.max(0, -getTooManyAllocatedTokens(f, t)));	//lower limit is 0
//			}
//		}
//		//return maximum in default case
//		return tg.getFlows().get(f).getTokensMax();
//	}
	
	private boolean moreTokensReleasedThanAllocated(int f, int t){
		return getTooManyAllocatedTokens(f, t)<0;
	}
	
	private int getTooManyAllocatedTokens(int f, int t){
		int ref_t = t;
		//heuristic to look up at another point in time, which corresponds to the vehicle location
		if(ADAPTIVE_loc){
			ref_t = getRefSlot(t, ng.getSlotChange(), f);
		}
		
		int to_allocate = getPlanned(f, ref_t) + getDropped(f) +getAddionallyAllocated(f, t);

		int f_id = tg.getFlows().get(f).getId();	//use f_id to identify corresponding flow in plan
		//when allocated less than so far planned (+dropped in plan), then impair
		int too_many_allocated_tokens= prefix_allocated_f_t[f_id][t] - to_allocate;	
		return too_many_allocated_tokens;
	}
	
	private boolean transmissionInSlot(int f, int t){
		
		int ref_t = t;
		//heuristic to look up at another point in time, which corresponds to the vehicle location
		if(ADAPTIVE_loc){
			ref_t = getRefSlot(t, ng.getSlotChange(), f);
		}

		boolean transmission_in_slot = false;
		if(ADAPTIVE_transm)transmission_in_slot=getPlanned(f, ref_t)>getPlanned(f, ref_t-1);
		return transmission_in_slot;
	}

	protected boolean oppScheduleDecision(int f, int n, int t) {
		//return false if index out of bounds
		if(t>=ng.getTimeslots() || n>=ng.getNetworks().size() || f>=tg.getFlows().size()) return false;
		
		int sum = getEstimatedSchedulingCost(f, n, t);
		int limit = getScheduleDecisionLimit(f, t);
//		if(f==0)System.out.println("Heuristic: oppScheduleDecision: f="+f+", n="+n+", t="+t+", sum"+sum+", limit="+limit);

		return  sum	< limit;

	}
	
	/**
	 * 
	 * @param f
	 * @param n
	 * @param t
	 * @return	estimated cost for scheduling one token of flow f to network n in time slot t. Should be negative for a benefit.
	 */
	public int getEstimatedSchedulingCost(int f, int n, int t){
		if(cs==null){
			initCostSeparation();
		}
		int statelessReward = cs.getStatelessReward(f);
		int statefulReward = cs.getStatefulReward(f, t);
		int netMatch = cs.getNetworkMatch(f, n);
		int timeMatch=cs.getTimeMatch(f, t);// weight is already covered in cs   // *tg.getFlows().get(f).getImpUser();

		int sum = statelessReward+statefulReward	//attracting
				+netMatch+timeMatch;	//repelling forces

		return sum;		//should be 
	}
	
	/**
	 * @return sorted list of flows ordered by decreasing criticality.
	 */
	public List<Integer> sortByFlowCriticality() {
		//		################# 1. sort flows according to decreasing criticality #################
				List<Integer> flowCriticality= new LinkedList<Integer>();
				//sort keys of map in descending order
				List<Integer> flow_order = new LinkedList<Integer>();
				for(int f=0; f<tg.getFlows().size(); f++){
//					Flow flow= tg.getFlows().get(f);
//					System.out.println("Flow "+f+": maxTokens="+flow.getTokensMax()+", winMax="+flow.getWindowMax());
					flow_order.add(f);
					flowCriticality.add(calculateFlowCriticality(tg.getFlows().get(f), ng));
				}
				final List<Integer> flowCrit_tmp=flowCriticality;
				Collections.sort(flow_order, new Comparator<Integer>() {
					@Override
					public int compare(Integer i1, Integer i2) {
						return flowCrit_tmp.get(i2)-flowCrit_tmp.get(i1);
					}
				});	 //highest priority first
//				System.out.println(flow_order);

				tg.setFlowIndices();
		return flow_order;
	}
	
	
	/**
	 * criticality is worst case schedule cost (flow NOT scheduled) devided by the number of tokens 
	 * @param f flow
	 * @param ng available networks
	 * @return criticality
	 */
	public int calculateFlowCriticality(Flow f, NetworkGenerator ng){
		//calculate violation if flow is NOT scheduled (worst case)
		FlowGenerator tg_temp= new FlowGenerator();
		tg_temp.addFlow(f);
		CostFunction cf = new CostFunction(ng, tg_temp);
		//get cost with empty schedule (worst case, flow is unscheduled)
		int cost_wc = cf.costViolation(getEmptySchedule(tg_temp, ng));
		int loglen = (int)Math.log(f.getDeadline()-f.getStartTime())+1;
//		System.out.println("loglen f"+f.getId()+": "+loglen);
//		System.out.println("crit f"+f.getId()+": "+cost_wc/f.getTokens()+", corr "+cost_wc/(loglen*f.getTokens()));
		if(f.getTokens()>0)
			return cost_wc/(loglen*f.getTokens());//-cost_bc;
		else
			return 0;
	}
	

	private int[][][] getEmptySchedule(FlowGenerator tg, NetworkGenerator ng){
		return new PriorityScheduler(ng, tg).getEmptySchedule();	//this could be a dummy scheduler.. only need empty schedule from it
	}
	
	/**
	 * rates for the flow and the remaining capacity of the networks, which networks fit best
	 * @param flow 
	 */
	protected Vector<Integer> sortNetworkIDs(final int f){
		//create sorted list
		Vector<Integer> netIDs = new Vector<>();
		for(int n = 0; n<ng_tmp.getNetworks().size(); n++){		//use ng_tmp: remaining capacity of networks
			netIDs.add(n);
		}
		
		//comparator which compares match of two networks to flow
		//uses latency, jitter and throughput
		Collections.sort(netIDs, new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				int result=cs.getNetworkMatch(f, arg0)-cs.getNetworkMatch(f, arg1);		//calcVio(f, arg0)-calcVio(f,arg1);
				//in case of equal match, use network with higher remaining average throughput
				if(result==0){
					Network net0 = ng_tmp.getNetworks().get(arg0);
					Network net1 = ng_tmp.getNetworks().get(arg1);
					return getAvCapacity(net1)-getAvCapacity(net0);
				}else
				return result;
			}
		}
		);
//		System.out.println("network sort for Flow "+flow.getId()+" = "+netIDs);
		return netIDs;
	}
	
	/**
	 * makes a copy of each network and rates it the match once with and once without throughput
	 * rates for the flow and the remaining capacity of the networks, which networks fit best
	 * @param flow 
	 */
	protected Vector<Integer> sortNetworkIDs2(int f){
		//create sorted list
		Vector<Integer> netIDs = new Vector<Integer>();
		Vector<Integer> benefit = new Vector<Integer>();
		//add network IDs with default match
		for(int n = 0; n<ng_tmp.getNetworks().size(); n++){		//use ng_tmp: remaining capacity of networks
			netIDs.add(n);
			benefit.add(cs.getNetworkMatch(f, n));
		}
		
		//add network IDs with match without stateful reward
		//get maximum stateful reward
		int maxStatefulReward=Integer.MAX_VALUE;
		for(int t=0;t<ng.getTimeslots();t++){
			maxStatefulReward=Math.min(maxStatefulReward, cs.getStatefulReward(f, t));
//			System.out.println("Heuristic: f="+f+", t="+t+", stateful reward="+cs.getStatefulReward(f, t));
		}
		for(int n = 0; n<ng_tmp.getNetworks().size(); n++){		//use ng_tmp: remaining capacity of networks
			netIDs.add(n+ng_tmp.getNetworks().size());
			benefit.add(cs.getNetworkMatch(f, n)+maxStatefulReward);
		}

		final Vector<Integer> benefit0= benefit;
		//comparator which compares match of two networks to flow
		//uses latency, jitter and throughput
		Collections.sort(netIDs, new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				int result = benefit0.get(arg0)-benefit0.get(arg1);
//				int result=calcVio(f, arg0)-calcVio(f,arg1);
				//in case of equal match, use network with higher remaining average throughput
				if(result==0){
					if(arg0>=ng_tmp.getNetworks().size())arg0-=ng_tmp.getNetworks().size();
					if(arg1>=ng_tmp.getNetworks().size())arg1-=ng_tmp.getNetworks().size();
					Network net0 = ng_tmp.getNetworks().get(arg0);
					Network net1 = ng_tmp.getNetworks().get(arg1);
					return getAvCapacity(net1)-getAvCapacity(net0);
				}else
				return result;
			}
		}
		);
		
		for(int n=0;n<netIDs.size();n++){
			if(netIDs.get(n)>=ng_tmp.getNetworks().size()){
				netIDs.set(n, netIDs.get(n)-ng_tmp.getNetworks().size());
			}
		}
//		System.out.println("network sort for Flow "+flow.getId()+" = "+netIDs +"\n  with benefits "+benefit);
		return netIDs;
	}
	
	
	//return the average throughput of the network over active slots (>0)
	private int getAvCapacity(Network net){
		int sumCap= 0;
		int sumSlots =0;
		for(int t = 0; t<ng.getTimeslots();t++){
			if(net.getCapacity().get(t)>0){
				sumCap+=net.getCapacity().get(t);
				sumSlots++;
			}
		}
		if (sumSlots==0) 
			return 0;
		else
			return sumCap/sumSlots;
	}
	
	/**
	 * ################ Heuristics for Adaptation: ##################
	 * 
	*/
	
	/**
	 * prefix sum counts the sum over all networks and over all previous time slots
	 * @return false if calculation failed (long term plan does not exist) 
	 */
	private boolean initializePrefixSum() {
//		System.out.println("HeuristicS: longTermSP="+showSchedule(longTermSP_f_t_n));
		if(longTermSP_f_t_n==null) return false;

		prefixSumLongTermSP_fid_t=new int[longTermSP_f_t_n.length][longTermSP_f_t_n[0].length];
		additionallyAllocated_f_t=new int[tg.getFlows().size()][ng.getTimeslots()];
				
		for(int f=0; f<longTermSP_f_t_n.length; f++){
			//prefix sum counts the sum over all networks and over all previous time slots
			int prefixSumOverNets=0;
			for(int t=0; t<longTermSP_f_t_n[0].length; t++){
				//over networks
				for(int n=0; n<longTermSP_f_t_n[0][0].length; n++){
					prefixSumOverNets+=longTermSP_f_t_n[f][t][n];
				}
				//after summing over networks, apply to the time slot
				prefixSumLongTermSP_fid_t[f][t]=prefixSumOverNets;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param f
	 * @param t0
	 * @param tokens
	 */
	protected void addAdditionallyAllocated(int f, int t, int tokens){
		if(ADAPTIVE_add && additionallyAllocated_f_t!=null){
			//if there are no more tokens released than allocated, the transmission would be forbidden
			//if in this case transmission in time slot triggers transmission, then this is additional data,
			//which is out of the plan
			
			if(!moreTokensReleasedThanAllocated(f, t) && transmissionInSlot(f, t))
			for(int t0=t; t0<additionallyAllocated_f_t[0].length;t0++){
				additionallyAllocated_f_t[f][t0]+=tokens;
			}
		}
	}
	
	private int getAddionallyAllocated(int f, int t){
		if(additionallyAllocated_f_t==null) return 0;
		return additionallyAllocated_f_t[f][t];
	}

	int getStartTime(Flow f){
		int st=0;
		if(f.getImpStartTime()>0){
			st=  f.getStartTime();
		}
		return Math.max(0, st-1);
	}
	
	int getDeadline(Flow f){
		int dl=ng.getTimeslots();
		if(f.getImpDeadline()>0){
			dl= f.getDeadline();
		}
		return Math.min(ng.getTimeslots(), dl);
	}

	public void loadLongTermSp(String logfile){
		if(new File(logfile).exists()){
			this.longTermSP_f_t_n = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logfile);
			initDropped();
			initializePrefixSum();
		}
	}
	
	/**
	 * 
	 * @param f of fg. is internally translated to f_id
	 * @return number of non-scheduled tokens in the plan for this flow (f_id)
	 */
	private int getDropped(int f){
		Flow flow = tg.getFlows().get(f);
		if(flow.getId()<dropped_f.length){
			return dropped_f[flow.getId()];
		}else{
			return flow.getTokens();
		}
	}
	
	/**
	 * 
	 * @param f of fg. is internally translated to f_id
	 * @param t 
	 * @return number of tokens of flow f_id which have been planned to transmit till time slot t 
	 */
	private int getPlanned(int f, int t){
		t=Math.max(0, Math.min(t, ng.getTimeslots()-1));
		Flow flow = tg.getFlows().get(f);
		if(flow.getId()<dropped_f.length){	//for new new flow, return 0 
			return prefixSumLongTermSP_fid_t[flow.getId()][t];
		}else{
			return 0;
		}
	}
	
	private void initDropped(){
		dropped_f = new int[longTermSP_f_t_n.length];
		//dropped_allocated_f=new int[longTermSP_f_t_n.length];
		
		for(int f=0; f<longTermSP_f_t_n.length; f++){
			int dropped_tokens=tgPred.getFlows().get(f).getTokens();
		
			for(int t= 0; t<longTermSP_f_t_n[0].length;t++){
				for (int n=0; n<longTermSP_f_t_n[0][0].length; n++){
					dropped_tokens-=longTermSP_f_t_n[f][t][n];
				}
			}
			dropped_f[f]=dropped_tokens;
		}
	}
	
	
	/**
	 * 
	 * @param f
	 * @param t
	 * @return the error of k time slots before t. k is equal to 
	 */
	protected double getError(int f, int t){
		if(!ADAPTIVE_err)return 0;
		t=Math.min(t, ng.getTimeslots()-1);
		int win=ng.getTimeslots()/4;
		Flow flow= tg.getFlows().get(f);
		if(flow.getImpThroughputMin()>0){
			win= Math.min(win,flow.getWindowMin());
		}
		
		int t_min = Math.max(0, t-win);		//how to select the observer window of 10?
		
//		double err_move =ng.getPositionError(ng.getSlotChange(), t);
//		double err_net = ng.getNetworkError(ngPred.getNetworks(), t_min, t);
		
		UncertaintyErrorCalculation uec = new UncertaintyErrorCalculation(tgPred.getFlows(), tg.getFlows(), ng.getTimeslots());
//		double err_flow= uec.getFlowUncertaintyError2(f,t_min, t)+uec.getFlowUncertaintyError(t_min, t)/2;
//		
//		double sum_err = err_net+2*err_flow;
//		double err=Math.pow(sum_err,2); //TODO : define better function of move, net and flow error
//		System.out.println("Heuristic: getError: f="+f+", t="+t+", win="+win+", err="+uec.getFlowUncertaintyError2(f,t_min, t));
		return uec.getFlowUncertaintyError2(f,t_min, t);
//		return err;
	}
	
	
	/**
	 * 
	 * @param t
	 * @param slotChange
	 * @param f
	 * @return return the time slot t_ref of the plan which corresponds to the current location of the vehicle. 
	 * 			Limit difference  between t and t_ref by throughput time window of the data flow f. 
	 */
	private int getRefSlot(int t, Vector<Integer> slotChange, int f){
		if(slotChange==null) return t;
		//range is the lower one of flow window (flexibility) and movement error in slots
		int range = Math.min(tg.getFlows().get(f).getWindowMin(), Math.abs(slotChange.get(t)-t));
		
		if(slotChange.get(t)>t){
			//vehicle moved faster. release more tokens from future to match network availability timin
			return t+range;	
		}else{
			//vehicle moved slower. release less tokens
			return t-range;
		}
		
	}
	
	protected String getTypeExt(){
		String ext = typeExt;
		if(ADAPTIVE_err) ext+="_err";
		if(ADAPTIVE_loc) ext+="_loc";
//		if(ADAPTIVE_transm) ext+="_tr";
		if(ADAPTIVE_add) ext+="_add";
		return ext;
	}
	
	/**
	 * location reference of transmission plan.
	 * Instead of refering to the tp in time dimension, refer to it in spacial dimension. 
	 * Thus, look up the time which corresponds to the current location of the vehicle
	 * @param adapt "true" activates mechanism
	 * @return	this scheduler
	 */
	public HeuristicScheduler adapt_location(boolean adapt){
		ADAPTIVE_transm = adapt;	//combine these two. do not use separately
		ADAPTIVE_loc = adapt;
		return this;
	}
	
	/**
	 * react on flow prediction error. Decrease threshold of opportunistic decision.
	 * @param adapt
	 * @return
	 */
	public HeuristicScheduler adapt_err(boolean adapt){
		ADAPTIVE_err = adapt;
		return this;
	}

	public HeuristicScheduler adapt_add(boolean adapt){		//seems to be not beneficial
		ADAPTIVE_add = adapt;
		return this;
	}
}
