package schedulers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;


public class PriorityScheduler extends Scheduler{
	

	@Override
	public String getType() {
		return "priority";
	}
	
	/**
	 * 
	 * Selects a network randomly and keeps it as long as any data can be scheduled to it
	 * 
	 * Behavior:
	 * 
	 * 1. Order flows according to userPreference
	 * 2. Schedule flows in this order
	 * 		2.1. Order networks for each run according to constraints match
	 * 		2.2. Try to allocate as much as possible to highest priority network, second highest network,.. within flow start time till flow deadline 
	 * 		2.3. Continue with next flow
	 * 
	 * schedule_f_t_n
	 */

	public PriorityScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected void calculateInstance_internal(String logfile) {
		
//		1. sort flows
		HashMap<Integer, Vector<Integer>> userPreftoFlows= new HashMap<Integer,Vector<Integer>>();
		for(int f=0; f<tg.getFlows().size(); f++){
			int userPref=tg.getFlows().get(f).getImpUser();
			if(userPreftoFlows.containsKey(userPref)){
				userPreftoFlows.get(userPref).add(f);		//add flow ID to userPref key
			}else{
				Vector<Integer> flowIds=new Vector<Integer>();
				flowIds.add(f);
				userPreftoFlows.put(userPref, flowIds);
			}
		}
		//sort keys of map in descending order
		ArrayList<Integer> sortedKeys = new ArrayList<Integer>();
		Collections.sort(sortedKeys, Collections.reverseOrder());	 //highest priority first
		
		//start allocation for each flow
		for(int f= 0; f<tg.getFlows().size(); f++){
			
			Flow flow = tg.getFlows().get(f);
			Set<Integer> usedSlots = new HashSet<Integer>(); 
			int chunksMaxTp = (int)(flow.getTokensMax()/flow.getWindowMax());	//get average maximum throughput for later allocation
			int chunksToAllocate = flow.getTokens();
			
			//sort networks according to match with flow
			Vector<Integer> networkIDs = sortNetworkIDs(flow);

			//try to allocate in networks in descending order according to match
			for(int n =0; n<ng.getNetworks().size() && chunksToAllocate>0; n++){
				int n0=networkIDs.get(n);
				//System.out.println("##########~~~~~~Flow "+f+", vio (steigend) "+f+" --> vio:"+calcVio(flow, ng.getNetworks().get(networkIDs.get(n))));
				//allocate in this network between start time and deadline of flow
				//do not allocate more than once in same time slot
				for(int t=flow.getStartTime(); t<flow.getDeadline() && chunksToAllocate>0;t++){
					if(!usedSlots.contains(t)){
						int allocated=0;
						if(chunksToAllocate<chunksMaxTp){
							allocated=allocate(f, t, n0, chunksToAllocate); //do not allocate more chunks than required
						}else{
							allocated=allocate(f, t, n0, chunksMaxTp);
						}	
						//System.out.println(chunksToAllocate);
						chunksToAllocate-=allocated;
						if(allocated>0){
							usedSlots.add(t);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param flow 
	 */
	private Vector<Integer> sortNetworkIDs(final Flow flow){
		//create sorted list
		Vector<Integer> netIDs = new Vector<>();
		for(int n = 0; n<ng.getNetworks().size(); n++){
			netIDs.add(n);
		}
		
		//comparator which compares match of two networks to flow
		//uses latency, jitter and throughput
		Collections.sort(netIDs, new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				Network net0 = ng.getNetworks().get(arg0);
				Network net1 = ng.getNetworks().get(arg1);
				
				return calcVio(flow, net0)-calcVio(flow,net1);
			}
		}
		);
		
		return netIDs;
	}
	
	private int calcVio(Flow flow, Network network){
		return 	CostFunction.jitterMatch(flow, network)+		//match functions return 0 for match, else strength of violation
				CostFunction.latencyMatch(flow, network)+
				throughputMatch(flow, network);	
	}
	
	/**
	 * Considers lower limit only; does not consider already used resources
	 * @param flow
	 * @param net
	 * @return approximation of fit between capacity of network to throughput requirements of flow (not cost function!)
	 */
	private int throughputMatch(Flow flow, Network net){
		//calculate average capacity per bucket neglecting zero-buckets
		int slotcount=0;
		int sum =0;
		for(int i:net.getCapacity()){
			if(i>0){
				sum+=i;
				slotcount++;
			}
		}
		int averageTp=Math.round(sum/slotcount);
		//get minimum throughput requirement of flow
		int flow_minTp = (int) Math.ceil(flow.getTokensMin()/flow.getWindowMin());
		int vio=(10*flow_minTp/averageTp);
		if(averageTp<flow_minTp){
			vio+=(int)Math.pow(flow_minTp-averageTp, 2)*flow.getImpThroughputMin();		//punish throughput violation quadratically	
		}
		return vio;	//more throughput leads to lower violation --> prefer high throughput networks slightly	
		
	}

}
