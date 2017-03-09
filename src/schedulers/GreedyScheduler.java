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


public class GreedyScheduler extends HeuristicScheduler{
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

	public GreedyScheduler(NetworkGenerator ng, FlowGenerator tg) {

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
//			System.out.println("########### Flow "+flowIndex +"   Network "+n);
			//schedule min tp first
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
//						if(flow.getTokensMin()/flow.getWindowMin()>0){
//							chunks=(int)(flow.getTokensMin()/flow.getWindowMin());
////							System.out.println("MIN LIMIT HOLDS FOR FLOW "+flowIndex+": "+chunks);
//						}
						allocated=allocate(flowIndex, t, n, Math.min(chunksToAllocate, chunks)); //do not allocate more chunks than required and flow can provide
						chunksToAllocate-=allocated;

						//update internal state of allocation
						if(allocated>0){
//							System.out.println("allocated f "+flowIndex+", t "+t+", net "+n+": "+allocated);
							if(NEW_RATING_ESTIMATOR){
								cs.updateStatefulReward(flowIndex, t, allocated);
							}
//							System.out.println("allocated "+allocated+ " t="+t+" n="+n0 +" remaining "+chunksToAllocate);
							usedSlots.add(t);	//mark slot as used
							//remove capacity from network
							int remaining_chunks=ng_tmp.getNetworks().get(n).getCapacity().get(t)-allocated;
							ng_tmp.getNetworks().get(n).getCapacity().set(t,remaining_chunks);

						}
					}
				}
			}
	
		}
		return 0;
	}
	

	@Override
	public String getType() {
		String s=new String("Greedy_"+schedule_decision_limit+(NEW_RATING_ESTIMATOR?"_H2":"")).replace("-", "m");
		return s;
	}

}
