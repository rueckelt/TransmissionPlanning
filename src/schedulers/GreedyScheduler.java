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
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;


public class GreedyScheduler extends Scheduler{
	private final boolean RELAX_TIME_LIMITS = false;

	@Override
	public String getType() {
		return "greedyFill";
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

	@Override
	protected void calculateInstance_internal(String logfile) {
		
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
		
		//############## 2. start allocation for each flow #################
		for(int f= 0; f<tg.getFlows().size(); f++){
			int f0 = flow_order.get(f);
			System.out.println("flow "+f0+" criticality decerasing: "+ flowCriticality.get(f0));
			
			scheduleFlow(f0, false);
		}
		System.out.println("greedy done");
	}

	/**
	 * @param flowId
	 * @param rate boolean if you just need the rating (true) or if it should be scheduled (false)
	 * rate=true deletes the current schedule. It should be used only on fresh instances/before scheduling. 
	 */
	private int scheduleFlow(int flowId, boolean rate) {
		Flow flow = tg.getFlows().get(flowId);	
		Set<Integer> usedSlots = new HashSet<Integer>(); 
		int chunksMaxTp = (int)(flow.getChunksMax()/flow.getWindowMax());	//get average maximum throughput for later allocation
		int chunksToAllocate = flow.getChunks();
		
		//sort networks according to match with flow
		Vector<Integer> networkIDs = sortNetworkIDs(flow);
			System.out.println("Network order: "+networkIDs);


//############## 2. Network choice according to flow matching #################
		//try to allocate in networks in descending order according to match
		for(int n =0; n<ng.getNetworks().size() && chunksToAllocate>0; n++){
			int n0=networkIDs.get(n);
			Network net = ng_tmp.getNetworks().get(n0);
			
			//do only allocate if allocation leads to cost reduction
			if(calcVio(flow, net)<0){
			//allocate in this network between start time and deadline of flow
			//do not allocate more than once in same time slot
				
				for(int t=getStartTime(flow); t<=getDeadline(flow) && chunksToAllocate>0;t++){
					if(!usedSlots.contains(t)){
//					System.out.println("time match "+chunksToAllocate);
						int allocated=0;
						if(chunksToAllocate<chunksMaxTp){
							allocated=allocate(flowId, t, n0, chunksToAllocate); //do not allocate more chunks than required
						}else{
							allocated=allocate(flowId, t, n0, chunksMaxTp);
						}	
//						System.out.println(chunksToAllocate);
						chunksToAllocate-=allocated;
						if(allocated>0){
								System.out.println("allocated "+allocated+ " t="+t+" n="+n0 +" remaining "+chunksToAllocate);
							usedSlots.add(t);	//mark slot as used
							//remove capacity from network
							if(!rate){
								//rate=false
								int remaining_chunks=ng_tmp.getNetworks().get(n0).getCapacity().get(t)-allocated;
	//							System.out.println(remaining_chunks+" rem, alloc "+ allocated);
								ng_tmp.getNetworks().get(n0).getCapacity().set(t,remaining_chunks);
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

	/**
	 * criticality is worst case schedule cost (flow NOT scheduled) 
	 * @param f flow
	 * @param ng available networks
	 * @return criticality
	 */
	private int calculateFlowCriticality(Flow f, NetworkGenerator ng){
		//calculate violation if flow is NOT scheduled (worst case)
		//TODO: and subtract violation is flow is scheduled alone (best case)
		FlowGenerator tg_temp= new FlowGenerator();
		tg_temp.addFlow(f);
		CostFunction cf = new CostFunction(ng, tg_temp);
		//get cost with empty schedule (worst case, flow is unscheduled)
		int cost_wc = cf.costViolation(getEmptySchedule(tg_temp, ng));
		//cost_wc*=f.getImpUser();
		//System.out.println("criticality:cost of flow "+getId()+" worst: "+cost_wc);
		int cost_bc = 0;
		return cost_wc;//-cost_bc;
	}
	

	private int[][][] getEmptySchedule(FlowGenerator tg, NetworkGenerator ng){
		return new PriorityScheduler(ng, tg).getEmptySchedule();	//this could be a dummy scheduler.. only need empty schedule from it
	}
	
	/**
	 * rates for the flow and the remaining capacity of the networks, which networks fit best
	 * @param flow 
	 */
	private Vector<Integer> sortNetworkIDs(final Flow flow){
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
				int result=calcVio(flow, net0)-calcVio(flow,net1);
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
	 * 
	 * @param flow
	 * @param network
	 * @return estimation of cost for scheduling a chunk: negative, if allocation is expected to lead to profit 
	 */
	private int calcVio(Flow flow, Network network){
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
	
		for(int t = getStartTime(flow); t<getDeadline(flow); t++){	//only analyze throughput in time frame of flow
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
		int savedvio =tp*flow.getImpThroughputMin();	
		return savedvio;	
		
	}
	
	private int getAvMinTp(Flow flow){
		//get minimum throughput requirement of flow
		return (int) Math.ceil(flow.getChunksMin()/flow.getWindowMin());
	}
	
	private int getStartTime(Flow flow){
		if (RELAX_TIME_LIMITS){
			int startTime = flow.getStartTime()-15/(flow.getImpStartTime()+1);
			if(startTime<0){ startTime=0;}
			return startTime;
		} else{
			return flow.getStartTime();
		}
	}
	private int getDeadline(Flow flow){
		if (RELAX_TIME_LIMITS){
			int deadline = flow.getDeadline()+15/(flow.getImpDeadline()+1);
			if(deadline>=ng.getTimeslots()) {deadline=ng.getTimeslots();}
			return deadline;
		} else{
			return flow.getDeadline();
		}
	}

}
