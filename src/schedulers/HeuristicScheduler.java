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
		if(NEW_RATING_ESTIMATOR){
			int sum = getEstimatedSchedulingCost(f, n, t);
			if(f==0)System.out.println("Heuristic: oppScheduleDecision: f="+f+", n="+n+", t="+t+", sum"+sum);

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
		int tp=cs.getTimeMatch(f, t);// weight is already covered in cs   // *tg.getFlows().get(f).getImpUser();
		
		double time_impairing=0;
		if(TIME_IMPAIRING_WEIGHT!=0){
			time_impairing=getTimeImparing(f,t, 0);
		}
		if(f==0){
//			System.out.println("Heuristic: getEstSch: f="+f+", n="+n+", t="+t+", \t pos="+(statefulReward+statelessReward)+", neg="+(netMatch+tp)+", time_imp="+time_impairing);
		}
		
		int sum = (int) ((statelessReward+statefulReward)*(1-time_impairing))
				+netMatch+tp;	//repelling forces
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
	protected Vector<Integer> sortNetworkIDs(final Flow flow){
		//create sorted list
		Vector<Integer> netIDs = new Vector<>();
		for(int n = 0; n<ng_tmp.getNetworks().size(); n++){		//use ng_tmp: remaining capacity of networks
			netIDs.add(n);
//			System.out.println("N"+n+": "+ng_tmp.getNetworks().get(n).getCapacity().toString());
//			System.out.println("N"+n+": "+ng.getNetworks().get(n).getCapacity().toString());
		}
		
		//comparator which compares match of two networks to flow
		//uses latency, jitter and throughput
		Collections.sort(netIDs, new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				int result=calcVio(flow.getIndex(), arg0)-calcVio(flow.getIndex(),arg1);
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
			c= cs.getNetworkMatch(f, n) + cs.getStatelessReward(f);
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
	 * Heuristics for Adaptation:
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
	 * @param f flow index
	 * @param t time slot index
	 * @param allocated_f number of allocated tokens per flow
	 * @return	how much to impair attractive force based on schedules. 1 not at all. 0 strongly.
	 * use long term schedule 
	 */
	public double getTimeImparing(int f, int t, double bias){
		if(TIME_IMPAIRING_WEIGHT==0) return 1;
		//initialize prefix sum if not done yet
		if(prefixSumLongTermSP_f_t == null){
			boolean success = initializePrefixSum();
			if(!success) return 1.0;
		}
		
		if(f>=tgPred.getFlows().size()) return 1.0;		//if flow was added, do not impair
		t=Math.min(t,longTermSP_f_t_n[0].length-1);			//restrict t to array size.
		
		double impairing =0;
		
		//h_time is 0 when scheduling follows plan. Goes up to 1 when more tokens scheduled then planned
		//allocated_f is set in the allocate method of the "Scheduler" class

		double alpha = getTimeImpairingWeight();
		int win =2;// 5+(int) (Math.ceil(1/alpha));
		//window of 5 timeslots length.
		int t_min = Math.max(0, t-win);
		int t_max= Math.max(0, t+win-1);
		updatePrefixAllocated(t, f);
		int i=0;
		if(f==3 && t==21){
			i=0;
		}
		int alloc_dif= i+prefix_allocated_f_t[f][t]- prefix_allocated_f_t[f][t_min];
		int planned_dif= prefixSumLongTermSP_f_t[f][t_max] - prefixSumLongTermSP_f_t[f][t_min];
//		System.out.println("Alloc= "+Arrays.deepToString(prefix_allocated_f_t));
		
		double h_time;// = ((double)Math.max(0, alloc_dif-planned_dif))/Math.max(1,alloc_dif);
//		if(f==3)System.out.println("Heuristics: getTimeImpairing: prefixSum="+planned_dif+", alloc "+alloc_dif+"; h= "+(1-h_time));
		

//		System.out.println("HEURISTIC_TIME_IMPAIRING: t="+t+", f= "+f+", h_time="+h_time+",\t sum_LTSP = "+ prefixSumLongTermSP_f_t[f][t]
//				+",\t alloc="+allocated_f[f] + "\t alpha="+alpha);
		
		int sum=Math.max(0, planned_dif-alloc_dif);
		for(int n = 0; n<ng.getNetworks().size(); n++){
			sum+=longTermSP_f_t_n[f][t][n];
		}
		
		if(sum>0)
			h_time=0;	//sched TODO: make a function based on t difference and win
//			h_time = Math.abs(t-)
		else
			h_time=1;	//not sched
		
		
		impairing = alpha*h_time;
		if(f==3)System.out.println("HEURISTIC_TIME_IMPAIRING: t="+t+", f= "+f+", h_time="+h_time+", impairing = "+impairing);
		
		return impairing;
	}
	
	private void updatePrefixAllocated(int t_max, int f){

		prefix_allocated_f_t[f][0]=allocated_f_t[f][0];
		for(int t=1; t<=t_max; t++){
			prefix_allocated_f_t[f][t]=prefix_allocated_f_t[f][t-1]+allocated_f_t[f][t];
		}
	}
	
	private double getTimeImpairingWeight(){
		double alpha = TIME_IMPAIRING_WEIGHT;
		if(alpha <0){
			//automatic
		}
		return alpha;
	}

	/**
	 * prefix sum counts the sum over all networks and over all previous time slots
	 * @return false if calculation failed (long term plan does not exist) 
	 */
	private boolean initializePrefixSum() {
		System.out.println("HeuristicS: longTermSP="+showSchedule(longTermSP_f_t_n));
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

	
	public void loadLongTermSp(String logfile){
		if(new File(logfile).exists())
			this.longTermSP_f_t_n = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logfile);
	}
	
}
