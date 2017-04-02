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


public class GreedyScheduler_s extends HeuristicScheduler{
//	private final boolean RELAX_TIME_LIMITS = false;

	
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

	public GreedyScheduler_s(NetworkGenerator ng, FlowGenerator tg) {

		super(ng, tg);

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
//			System.out.println("flow "+f0+" criticality decerasing: "+ flow_order);
			
			scheduleFlow(f0, false);
		}
//		System.out.println("greedy done");
	}


	/**
	 * @param f
	 * @param rate boolean if you just need the rating (true) or if it should be scheduled (false)
	 * rate=true deletes the current schedule. It should be used only on fresh instances/before scheduling. 
	 */
	private int scheduleFlow(int f, boolean rate) {
		Flow flow = tg.getFlows().get(f);	
		Set<Integer> usedSlots = new HashSet<Integer>(); 	//each flow can use only one network in each time slot
		Set<Integer> statefulRewardUsedSlots = new HashSet<Integer>(); 	//mark all slots for which stateful reward is used.
		Set<Integer> statefulRewardusedNets = new HashSet<Integer>(); 	//mark nets for which skipping is completed (reward is used)
		
		
		int chunksMaxTp = (int)(flow.getTokensMax());///flow.getWindowMax());	//get average maximum throughput for later allocation
		int chunksToAllocate = flow.getTokens();
		//sort networks according to match with flow
		Vector<Integer> networkIDs = sortNetworkIDs2(f);
//			System.out.println("##################################  Flow "+f+"; Network order: "+networkIDs);
//		System.out.println(flow.toString());

//############## 2. Network choice according to flow matching #################
		//try to allocate in networks in descending order according to match

//		System.out.println("Wird ausgefuehrt; chunks: "+chunksToAllocate+" networks size "+ng.getNetworks().size());
		for(int n1 =0; n1<networkIDs.size() && chunksToAllocate>0; n1++){
			int n=networkIDs.get(n1);
			//schedule min tp first
			for(int t=getStartTime(flow); (t<getDeadline(flow)) && chunksToAllocate>0; t++){
				
				int allocated;
				int t_skipping=0;
				
				do{
					int ts=t+t_skipping;
					int skipping_len=1;

					//do not allocate more than once in same time slot
					if(!usedSlots.contains(ts)){
						//do only allocate if allocation leads to cost reduction
						if(scheduleDecision(f, n, ts)){
						
							
	//					System.out.println("remaining chunks "+chunksToAllocate);
							allocated=0;
	//						System.out.println("chunks maxTP: "+chunksMaxTp+"; chunksToAllocate: "+chunksToAllocate);
							int chunks=chunksMaxTp;
							
							//skipping mode: allocate only when reward is available
							if(!statefulRewardusedNets.contains(n)){	
								if(statefulRewardUsedSlots.contains(ts)){
									chunks=0;
								}
							}
	
							allocated=allocate(f, ts, n, Math.min(chunksToAllocate, chunks)); //do not allocate more chunks than required and flow can provide
							chunksToAllocate-=allocated;
								
							//update internal state of allocation
							if(allocated>0){
	//							System.out.println("allocated f "+flowIndex+", t "+t+", net "+n+": "+allocated);
								if(NEW_RATING_ESTIMATOR){
									cs.updateStatefulReward(f, ts, allocated);
								}
	//							System.out.println("allocated "+allocated+ " t="+t+" n="+n0 +" remaining "+chunksToAllocate);
								usedSlots.add(ts);	//mark slot as used
								//remove capacity from network
								int remaining_chunks=ng_tmp.getNetworks().get(n).getCapacity().get(ts)-allocated;
								ng_tmp.getNetworks().get(n).getCapacity().set(ts,remaining_chunks);
	
								//skipping reward update
								skipping_len=Math.max(1, flow.getWindowMin()*allocated/flow.getTokensMin());
								if(statefulRewardusedNets.contains(n)){
									//mark time slots for which reward is already used from any allocation
									//this forbids allocation in skipping mode for parallel networks when reward is already used
									int min_win=Math.max(0, ts-skipping_len+1);
									int max_win=Math.max(ng.getTimeslots()-1, ts+skipping_len-1);
									for(int t_r=min_win; t_r<=max_win; t_r++){
										statefulRewardUsedSlots.contains(ts);	
									}
								}
								if(f==3){
//									System.out.println("Greedy_s: allocated f="+f+", n="+n+", t="+ts+", alloc="+allocated+", skipping="+(skipping_len));
									cs.printStatefulReward();
								}
							}
						}
					}
					t_skipping+=skipping_len;
				}while(t+t_skipping<flow.getDeadline());
			}
	
		}
		return 0;
	}
	

	@Override
	public String getType() {
		String s=new String("JTP_s").replace("-", "m");
		return s;
	}

}
