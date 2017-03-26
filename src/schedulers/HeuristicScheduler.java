package schedulers;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
	
	
	protected NetworkGenerator ng_tmp; //remove scheduled chunks from this ng
	protected int schedule_decision_limit =0;		//clim cost limit. positive fosters allocation. negative impairs allocation.
	protected int tl_offset =0;	//allowed time offset for which violation is allowed
	
	protected boolean NEW_RATING_ESTIMATOR=true;
	protected final boolean ADAPTIVE = true;
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
	private int[][] prefixSumLongTermSP_f_t;
	private int dropped_f[];
	private int dropped_allocated_f[];
	
	/**
	 * activates new rating scheme for cost estimation
	 * @param NEW_RATING
	 * @return
	 */
	public Scheduler newRating(boolean NEW_RATING){
		NEW_RATING_ESTIMATOR=NEW_RATING;
		tl_offset=0;	//allow the new metric to violoate time limit constraints
		return this;
	}
	
	protected void initCostSeparation(){
		cs= new CostSeparation(tg, ng);
	}


	protected boolean oppScheduleDecision(int f, int n, int t) {
		//return false if index out of bounds
		if(t>=ng.getTimeslots() || n>=ng.getNetworks().size() || f>=tg.getFlows().size()) return false;
		
		if(NEW_RATING_ESTIMATOR){
			int sum = getEstimatedSchedulingCost(f, n, t);
//			if(f==0)System.out.println("Heuristic: oppScheduleDecision: f="+f+", n="+n+", t="+t+", sum"+sum);

			return  sum	< schedule_decision_limit;

		}else
			return calcVio(f, n)<schedule_decision_limit;
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
		
		double time_impairing=0;
		if(TIME_IMPAIRING_WEIGHT!=0){
			time_impairing=getTimeImparing(f,t);
		}

		
		int sum = (int) ((statelessReward+statefulReward)*(1-time_impairing))
				+netMatch+timeMatch;	//repelling forces
		
//		if(f==5 && n==6){
//			System.out.println("Heuristic: getEstSch: f="+f+", n="+n+", t="+t+", \t pos="+
//					(statefulReward+statelessReward)+", neg="+(netMatch+timeMatch)+", time_imp="+time_impairing + "\t sum="+sum);
//		}
		return sum;		//should be 
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
	 * rates for the flow and the remaining capacity of the networks, which networks fit best
	 * @param flow 
	 */
	protected Vector<Integer> sortNetworkIDs2(int f){
		final Flow flow = tg.getFlows().get(f);
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
	
	
	/**
	 * We use this function only to rate network match. Not time match.
	 * @param flow
	 * @param network
	 * @return estimation of cost for scheduling a token of flow f to network n: negative, if allocation is expected to lead to profit 
	 */
	protected int calcVio(int f, int n){
		if(cs==null){
			initCostSeparation();
		}
		int c=0;
		if(NEW_RATING_ESTIMATOR){
			c= cs.getNetworkMatch(f, n) + cs.getStatelessReward(f);		//lcy+jit+stateless+monetary
//			if(f==5 && (n==1 || n==2 || n==6))
//				System.out.print("f="+f+", n="+n+", CALC_VIO: net_match = "+cs.getNetworkMatch(f, n)+"   stateless: "+cs.getStatelessReward(f)+"\n");
		}else{
			//old cost function estimation. Not used
		
			Flow flow = tg.getFlows().get(f);
			Network network = ng.getNetworks().get(n);
	//		if(true)
	//		return network.getId();
					//scheduling may lead to jitter and latency cost
			//TODO: violation is not per chunk!! it is for used time slots! normalize it.. but how?!
			c= 	((CostFunction.jitterMatch(flow, network)	//match functions return 0 for match, else strength of violation
					+ CostFunction.latencyMatch(flow, network))/(getAvMinTp(flow)+1)	
					
					//scheduling avoids throughput-violation cost and unsched cost
					- throughputMatch(flow, network)
					- flow.getImpUnsched()*flow.getImpUser()	//each unscheduled chunk leads to this cost
					)*flow.getImpUser()
					+ network.getCost()*ng.getCostImportance();	//cost independent from flow user weight
//			if(f==5 && (n==1 || n==2 || n==6))
//			System.out.println("f="+flow.getId()+", n="+ n+" jit "+CostFunction.jitterMatch(flow, network)*flow.getImpUser()+		//match functions return 0 for match, else strength of violation
//					" lcy "+CostFunction.latencyMatch(flow, network)*flow.getImpUser()+
//					" - tp_min "+throughputMatch(flow, network)*flow.getImpUser() + " cost "+network.getCost()*ng.getCostImportance() +
//					" - unsched "+flow.getImpUnsched()*flow.getImpUser()*flow.getImpUser()+ " c "+c);
			
			
		}
		return c;
	}
	
	/**
	 * Can the network transport the minimum throughput required for the flow?

	 * Considers lower limit only; does not consider already used resources
	 * @param flow
	 * @param net
	 * @return approximation of how much cost can be saved per chunk if chunk is scheduled
	 * TODO: I blame this function to overestimate minTp cost.. need evidence!
	 */
	private int throughputMatch(Flow flow, Network net){
		//calculate average capacity of a network per bucket neglecting zero-buckets
		int slotcount=0;
		int sum =0;
	
		for(int t = flow.getStartTime(); t<flow.getDeadline(); t++){	//only analyze throughput in time frame of flow
			int slotCapacity=net.getCapacity().get(t);
			if(slotCapacity>0){
				sum+=slotCapacity;	//sum up slot capacity
				slotcount++;		//count number of summarized slots
			}
		}
		int averageTp=0;
		if(slotcount>0){
			averageTp=Math.round(sum/slotcount);
		}
		int flow_minTp = getAvMinTp(flow);
		
		int tp;
		if(averageTp<flow_minTp){
			tp=averageTp;
		}else{
			tp = flow_minTp;
		}
		int savedvio =cf.vioTp_weight(tp, flow);	
		return savedvio;	
		
	}
	
	protected int getAvMinTp(Flow flow){
		//get minimum throughput requirement of flow
		return (int) Math.ceil(flow.getTokensMin()/flow.getWindowMin())+1;
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
	 * 1. Time match heuristic
	 * 
	 * Idea: Adapt the attracting forces of the model (throughput+unscheduled) according of the token to schedule to the long term schedule
	 *  --> decreases attracting forces, when more tokens are scheduled than planned. Move allocation farther away.
	 * def: planned tokens = in long-term plan
	 * 		
	 * 				Max( so far planned tokens - so far scheduled tokens , 0)
	 * h(f, t) = 	-----------------------------------------------------------	+ bias_time
	 * 					tokens of the data flow to schedule
	 * 
	 * supremum of h(f,t) rises till 1, when more tokens are planned then scheduled	--> no allocation impairing
	 * infimum of  h(f,t) is 0, when less tokens are scheduled than planned 	--> allocation impairing! foster delay!
	 * 						 why not negative? would foster allocation in future because it did not happen in past. but it may be better to delay beyond the horizon.
	 * 
	 * --> bias in [0, 1]
	 * 		 --> bias= 0 bias means, that planning to all other beneficial networks is ok as well
	 * 		---> bias= 0.3 means, that networks with scheduling gain of 70% of current are also ok
	 * 
	 * set:
	 * scheduling_limit = attracting forces * (alpha*h(f,t)
	 *  
	 * 	alpha says, how strong this should be.
	 *  we set it to an error estimation, using SMAPE.
	 *  
	 *   When prediction has been good, alpha should be 
	 */
	
	/**
	 * 
	 * @param f
	 * @param t
	 * @return the error of k time slots before t. k is equal to 
	 */
	protected double getError(int f, int t){
		if(!ADAPTIVE)return 0;
		
		int win=ng.getTimeslots()/4;
		Flow flow= tg.getFlows().get(f);
		if(flow.getImpThroughputMin()>0){
			win= flow.getImpThroughputMin();
		}
		
		int t_min = Math.max(0, t-win);		//how to select the observer window of 10?
		
		double err_move =ng.getPositionError(ng.getSlotChange(), t);
		System.out.println(ng.getSlotChange());
		double err_net = ng.getNetworkError(ngPred.getNetworks(), t_min, t);
		UncertaintyErrorCalculation uec = new UncertaintyErrorCalculation(tgPred.getFlows(), tg.getFlows(), ng.getTimeslots());
		double err_flow= uec.getFlowUncertaintyError2(t_min, t);
		
		
		double err=(err_net+err_flow)/2; //TODO : define better function of move, net and flow error
		
//		if(!set.contains(f+100*t))System.out.println("Heuristics: getError. err="+err+", move="+err_move+", net="+err_net+", flow="+err_flow);

		set.add(f+100*t);
		return err;
	}
	HashSet<Integer> set=new HashSet<Integer>(); 
	

	
	/**
	 * 
	 * impairing forbids the opportunistic approach to allocate tokens that are not planned in the long term plan.
	 * 
	 * @param f flow index
	 * @param t time slot index
	 * @param allocated_f number of allocated tokens per flow
	 * @return	how much to impair attractive force based on schedules. 1 not at all. 0 strongly.
	 * use long term schedule 
	 */
	protected double getTimeImparing(int f, int t){
		if(TIME_IMPAIRING_WEIGHT==0) return 1;
		if(isnewFlow(f)) return 0;	//schedule new flows opportunistically
		if(t>=ng.getTimeslots())return 0;
		
		//in case of continued flow, f_id might differ from f. 
		//Then use f for actual and f_id for predicted. They identify the same flow, but split
		int f_id = tg.getFlows().get(f).getId();	
		
		//initialize prefix sum if not done yet
		if(prefixSumLongTermSP_f_t == null){
			boolean success = initializePrefixSum();
			if(!success) return 1.0;
		}
		
//		if(f>=tgPred.getFlows().size()) return 1.0;		//if flow was added, do not impair
		t=Math.min(t,longTermSP_f_t_n[0].length-1);			//restrict t to array size.
		
		double impairing =0;
		
		//h_time is 0 when scheduling follows plan. Goes up to 1 when more tokens scheduled then planned
		//allocated_f is set in the allocate method of the "Scheduler" class
		
		//a value of 0 leads to opportunistic, a value of 1 forbids transmission in order to follow the plan.
		double alpha = getTimeImpairingWeight(f,t);
//		System.out.println("Heuristic:airing(f="+f+",n="+t+") alpha ="+alpha);
		int win =getWindow(f,t);
		//window of 5 timeslots length.
		int t_min = Math.max(0, t-win);
		t_min=0;
		int t_max= Math.min(ng.getTimeslots()-1, Math.max(0, t+win-1));
		updatePrefixAllocated(t, f);

		int alloc_dif= prefix_allocated_f_t[f][t]- prefix_allocated_f_t[f][t_min];
		int planned_dif= prefixSumLongTermSP_f_t[f_id][t_max] - prefixSumLongTermSP_f_t[f_id][t_min];
//		System.out.println("Alloc= "+Arrays.deepToString(prefix_allocated_f_t));
		
		double h_time;// = ((double)Math.max(0, alloc_dif-planned_dif))/Math.max(1,alloc_dif);
		
		
		//update dropped allocated: tokens that have been dropped 
		//in the plan but allocated in the opportunistic approach
		if(t>0){
			dropped_allocated_f[f]=Math.max(dropped_allocated_f[f], //max of dropped and 
					prefix_allocated_f_t[f][t-1]-prefixSumLongTermSP_f_t[f_id][t-1]); //diff of allocated and planned
		}
//		System.out.println("HEURISTIC_TIME_IMPAIRING: t="+t+", f= "+f+", h_time="+h_time+",\t sum_LTSP = "+ prefixSumLongTermSP_f_t[f][t]
//				+",\t alloc="+allocated_f[f] + "\t alpha="+alpha);
		
		int sum=Math.max(0, planned_dif+dropped_f[f]-dropped_allocated_f[f]	-alloc_dif);
		for(int n = 0; n<ng.getNetworks().size(); n++){
			sum+=longTermSP_f_t_n[f_id][t][n];
		}
		
		if(sum>0)
			h_time=0;	//sched TODO: make a function based on t difference and win
//			h_time = Math.abs(t-)
		else
			h_time=1;	//not sched
		

//		if(f==3)System.out.println("Heuristics: getairing: prefixSum="+planned_dif+", alloc "+alloc_dif+"; h= "+(1-h_time));
		
		impairing = alpha*h_time;
//		if(f==3)System.out.println("HEURISTIC_TIME_IMPAIRING: t="+t+", f= "+f+", h_time="+h_time+", impairing = "+impairing);
		
		return impairing;
	}

	/**
	 * @param f
	 * @param t
	 * @return	the window (+/-) for which it is checked if the flow f was planned to be transmitted
	 */
	private int getWindow(int f, int t){
		if(WINDOW>0) return WINDOW;		//if fixed window is set, use it
		
		//if not set (-1), use adaptive approach and calculate automatically
		Flow flow= tg.getFlows().get(f);
		
		
		int win_tp_min =Math.min(flow.getWindowMin(),(int) (1+flow.getWindowMin()*getError(f, t)));
//		System.out.println("Heuristic win="+win_tp_min);
		return win_tp_min;
		
//		double err= getError(t);	//error between 0 and 2
//		return (int) (1+err*20);			//TODO: how to select the parameter or function to calculate the window???
	}

	
	private double getTimeImpairingWeight(int f, int t){
		double alpha = TIME_IMPAIRING_WEIGHT;
		double err =getError(f, t);
		
		System.out.println("Heuristic: timeImpairingWeight: f="+f+", t="+t+", alpha="+alpha+", err="+err);
		return (1-err)*alpha;
	}

	
	
	// ######################  AUXILLARY ######################
	
	private void updatePrefixAllocated(int t_max, int f){

		prefix_allocated_f_t[f][0]=allocated_f_t[f][0];
		for(int t=1; t<=t_max; t++){
			prefix_allocated_f_t[f][t]=prefix_allocated_f_t[f][t-1]+allocated_f_t[f][t];
		}
	}
	
	
	/**
	 * 
	 * @param f index of flow in tg
	 * @return identify from ID if flow is new.
	 */
	private boolean isnewFlow(int f){
		return tg.getFlows().get(f).getId()>=tgPred.getFlows().size();
	}
	
	
	/**
	 * prefix sum counts the sum over all networks and over all previous time slots
	 * @return false if calculation failed (long term plan does not exist) 
	 */
	private boolean initializePrefixSum() {
//		System.out.println("HeuristicS: longTermSP="+showSchedule(longTermSP_f_t_n));
		if(longTermSP_f_t_n==null) return false;
		
		prefixSumLongTermSP_f_t=new int[tg.getFlows().size()][ng.getTimeslots()];
		
		for(int f=0; f<longTermSP_f_t_n.length; f++){
			//prefix sum counts the sum over all networks and over all previous time slots
			int prefixSumOverNets=0;
			for(int t=0; t<longTermSP_f_t_n[0].length; t++){
				//over networks
				for(int n=0; n<longTermSP_f_t_n[0][0].length; n++){
					prefixSumOverNets+=longTermSP_f_t_n[f][t][n];
				}
				//after summing over networks, apply to the time slot
				prefixSumLongTermSP_f_t[f][t]=prefixSumOverNets;
			}
		}
		return true;
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
		}
	}
	
	private void initDropped(){
		dropped_f = new int[longTermSP_f_t_n.length];
		dropped_allocated_f=new int[longTermSP_f_t_n.length];
		
		for(int f=0; f<longTermSP_f_t_n.length; f++){
			int dropped_tokens=tg.getFlows().get(f).getTokens();
		
			for(int t= 0; t<longTermSP_f_t_n[0].length;t++){
				for (int n=0; n<longTermSP_f_t_n[0][0].length; n++){
					dropped_tokens-=longTermSP_f_t_n[f][t][n];
				}
			}
			dropped_f[f]=dropped_tokens;
		}
	}
	
}
