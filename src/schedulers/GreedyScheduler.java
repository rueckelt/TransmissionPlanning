package schedulers;
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


public class GreedyScheduler extends Scheduler{
//	private final boolean RELAX_TIME_LIMITS = false;

	@Override
	public String getType() {
		String s=new String("Greedy_"+schedule_decision_limit+(NEW_RATING_ESTIMATOR?"_H2":"")).replace("-", "m");
		return s;
	}
	
	/**
	 * 
	 * Selects a network randomly and keeps it as long as any data can be scheduled to it
	 * 
	 * Behavior:
	 * 
	 * 1. Order flows according to criticality
	 * 2. Schedule flows in this order
	 * 		2.1. Order networks for each flow according to constraints match
	 * 		2.2. Try to allocate as much as possible to highest priority network, second highest network,third... from flow-start-time till flow-deadline 
	 * 		2.3. Continue with next flow
	 * 
	 * schedule_f_t_n
	 */

	public GreedyScheduler(NetworkGenerator ng, FlowGenerator tg) {
		
		super(ng, tg);
		if(ng!=null&&tg!=null){
			ng_tmp=ng.clone();
		}
	}
	
	
	private NetworkGenerator ng_tmp; //remove scheduled chunks from this ng
	protected int schedule_decision_limit =0;
	protected int tl_offset =0;	//allowed time offset for which violation is allowed
	
	protected boolean NEW_RATING_ESTIMATOR=true;
	protected CostSeparation cs;
	
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


	@Override
	protected void calculateInstance_internal(String logfile) {
		if(NEW_RATING_ESTIMATOR){
			initCostSeparation();
		}
		
		List<Integer> flow_order = sortByFlowCriticality();
		//System.out.println("FlowOrder Gre: "+flow_order.toString());
		//############## 2. start allocation for each flow #################
		for(int f= 0; f<tg.getFlows().size(); f++){
			int f0 = flow_order.get(f);
//			System.out.println("flow "+f0+" criticality decerasing: "+ );
			
			scheduleFlow(f0, false);
		}
//		System.out.println("greedy done");
	}

	/**
	 * @return
	 */
	protected List<Integer> sortByFlowCriticality() {
		//		################# 1. sort flows according to decreasing criticality #################
				List<Integer> flowCriticality= new LinkedList<Integer>();
				//sort keys of map in descending order
				List<Integer> flow_order = new LinkedList<Integer>();
				for(int f=0; f<tg.getFlows().size(); f++){
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
				System.out.println(flow_order);
		return flow_order;
	}

	/**
	 * @param flowIndex
	 * @param rate boolean if you just need the rating (true) or if it should be scheduled (false)
	 * rate=true deletes the current schedule. It should be used only on fresh instances/before scheduling. 
	 */
	private int scheduleFlow(int flowIndex, boolean rate) {
		Flow flow = tg.getFlows().get(flowIndex);	
		Set<Integer> usedSlots = new HashSet<Integer>(); 	//each flow can use only one network in each time slot
		int chunksMaxTp = (int)(flow.getTokensMax());///flow.getWindowMax());	//get average maximum throughput for later allocation
		int chunksToAllocate = flow.getTokens();
		//sort networks according to match with flow
		Vector<Integer> networkIDs = sortNetworkIDs(flow);
//			System.out.println("##################################  Flow "+flowIndex+"; Network order: "+networkIDs);
//		System.out.println(flow.toString());

//############## 2. Network choice according to flow matching #################
		//try to allocate in networks in descending order according to match

//		System.out.println("Wird ausgefuehrt; chunks: "+chunksToAllocate+" networks size "+ng.getNetworks().size());
		for(int n1 =0; n1<ng.getNetworks().size() && chunksToAllocate>0; n1++){
			int n=networkIDs.get(n1);
			//Network net = ng_tmp.getNetworks().get(n);
//			System.out.println("########### Flow "+flowIndex +"   Network "+n);

			for(int t=Math.max(0, flow.getStartTime()-1-tl_offset); 
					t<Math.min(ng.getTimeslots(), flow.getDeadline()+tl_offset) && chunksToAllocate>0;
					t++){
				//do only allocate if allocation leads to cost reduction
				if(scheduleDecision(flowIndex, n, t)){
					
					//do not allocate more than once in same time slot
					if(!usedSlots.contains(t)){
//					System.out.println("remaining chunks "+chunksToAllocate);
						int allocated=0;
//						System.out.println("chunks maxTP: "+chunksMaxTp+"; chunksToAllocate: "+chunksToAllocate);
						int chunks=chunksMaxTp;
//						if(NEW_RATING_ESTIMATOR){
//							int rating = 	calcVio(flowIndex, n)+	//stateless reward + network match
////											cs.getStatefulReward(f, t)+
//											cs.getTimeMatch(flowIndex, t)/getAvMinTp(tg.getFlows().get(flowIndex));	
//							if(rating>schedule_decision_limit){
//								chunks=flow.getTokensMin();
//							}
//						}
//						if(chunksToAllocate<chunksMaxTp){
							allocated=allocate(flowIndex, t, n, Math.min(chunksToAllocate, chunks)); //do not allocate more chunks than required and flow can provide
//						}else{
//							allocated=allocate(flowIndex, t, n, chunksMaxTp); //do not allocate more than flow can provide 
//						}	
//						System.out.println("remaining tokens "+chunksToAllocate+"; allocated: "+allocated);
						chunksToAllocate-=allocated;
						
						//update internal state of allocation
						if(allocated>0){
							if(NEW_RATING_ESTIMATOR){
								cs.updateStatefulReward(flowIndex, t, allocated);
							}
//								System.out.println("allocated "+allocated+ " t="+t+" n="+n0 +" remaining "+chunksToAllocate);
							usedSlots.add(t);	//mark slot as used
							//remove capacity from network
							if(!rate){
								//rate=false
								int remaining_chunks=ng_tmp.getNetworks().get(n).getCapacity().get(t)-allocated;
	//							System.out.println(remaining_chunks+" rem, alloc "+ allocated);
								ng_tmp.getNetworks().get(n).getCapacity().set(t,remaining_chunks);
							}else{
								//rate=true
								int[][][] sched = getTempSchedule();	//hold schedule
								setTempSchedule(getEmptySchedule());	//reset schedule
								return new CostFunction(ng_tmp, tg).costViolation(sched);	//calculate rating
							}
							
						}
					}
				}
			}
		}
		return 0;
	}
	
	protected boolean oppScheduleDecision(int f, int n, int t) {
//		System.out.println("DECISION_VIO "+calcVio(flow, ng.getNetworks().get(n)));
		if(NEW_RATING_ESTIMATOR){
			int calcVio = calcVio(f,n);
			int statefulReward = cs.getStatefulReward(f, t);
			int tp=cs.getTimeMatch(f, t)*tg.getFlows().get(f).getImpUser();
			int sum = calcVio+statefulReward+tp;
//			if(sum-1000<schedule_decision_limit && sum>schedule_decision_limit){
//				System.out.println("Flow "+f+"\tat Time "+t+"\ton Net "+n+":\t"+(sum-schedule_decision_limit)+
//						",\t vio: "+calcVio+",\t stateful: "+statefulReward+",\t tp: "+tp+"\tnet-match: "+cs.getNetworkMatch(f, n));
//			}
			return  sum	< schedule_decision_limit;

		}else
			return calcVio(f, n)<schedule_decision_limit;
	}
	
	protected boolean scheduleDecision(int f, int n, int t) {
		return oppScheduleDecision(f, n, t);
	}
	
	public Scheduler setScheduleDecisionLimit(int limit){
		schedule_decision_limit=limit;
		return this;
	}

	/**
	 * criticality is worst case schedule cost (flow NOT scheduled) devided by the number of tokens 
	 * @param f flow
	 * @param ng available networks
	 * @return criticality
	 */
	protected int calculateFlowCriticality(Flow f, NetworkGenerator ng){
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
				Network net0 = ng_tmp.getNetworks().get(arg0);
				Network net1 = ng_tmp.getNetworks().get(arg1);
				int result=calcVio(flow.getIndex(), arg0)-calcVio(flow.getIndex(),arg1);
				//in case of equal match, use ID to create strict order
				if(result==0){
					return net0.getId()-net1.getId();
				}else
				return result;
			}
		}
		);
		
		return netIDs;
	}
	
	
	/**
	 * We use this function only to rate network match. Not time match.
	 * @param flow
	 * @param network
	 * @return estimation of cost for scheduling a token of flow f to network n: negative, if allocation is expected to lead to profit 
	 */
	protected int calcVio(int f, int n){
		if(NEW_RATING_ESTIMATOR){
			int c= cs.getNetworkMatch(f, n) + cs.getStatelessReward(f);
			//System.out.print("stateless: "+cs.getStatelessReward(f)+"\t");
			return c;
		}else{
			//old cost function estimation
		
			Flow flow = tg.getFlows().get(f);
			Network network = ng.getNetworks().get(n);
	//		if(true)
	//		return network.getId();
					//scheduling may lead to jitter and latency cost
			//TODO: violation is not per chunk!! it is for used time slots! normalize it.. but how?!
			int c= 	((CostFunction.jitterMatch(flow, network)	//match functions return 0 for match, else strength of violation
					+ CostFunction.latencyMatch(flow, network))/(getAvMinTp(flow)+1)	
					
					//scheduling avoids throughput-violation cost and unsched cost
					- throughputMatch(flow, network)
					- flow.getImpUnsched()*flow.getImpUser()	//each unscheduled chunk leads to this cost
					)*flow.getImpUser()
					+ network.getCost()*ng.getCostImportance();	//cost independent from flow user weight
			
		/*	System.out.println("vio flow "+flow.getId()+" net "+ network.getId()+" jit "+CostFunction.jitterMatch(flow, network)*flow.getImpUser()+		//match functions return 0 for match, else strength of violation
					" lcy "+CostFunction.latencyMatch(flow, network)*flow.getImpUser()+
					" - tp_min "+throughputMatch(flow, network)*flow.getImpUser() + " cost "+network.getCost()*ng.getCostImportance() +
					" - unsched "+flow.getImpUnsched()*flow.getImpUser()*flow.getImpUser()+ " c "+c);
			*/
			return c;
		}
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
	


}
