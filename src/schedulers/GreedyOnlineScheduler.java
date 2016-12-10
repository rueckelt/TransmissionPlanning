package schedulers;

import java.util.List;
import java.util.Vector;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;


/***
 * This scheduler makes network selection without any look into the future.
 * @author QZ61P8
 *
 */
public class GreedyOnlineScheduler extends GreedyScheduler {

	public GreedyOnlineScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Scheduler calculates network selection for each time slot separately:
	 * (1) select most critical flow
	 * (2) select best matching network available in current time slot
	 * (3) assign to network as much as possible, if assignment seems to be beneficial
	 */
	
	@Override
	protected void calculateInstance_internal(String logfile) {
		if(NEW_RATING_ESTIMATOR){
			initCostSeparation();
		}
		
		//initialize network preference vector
		Vector<Vector<Integer>> flowsToNets = new Vector<Vector<Integer>>();
		//initialize counter for remaining tokens of flows
		Vector<Integer> chunksToAllocate = new Vector<Integer>();
		for(Flow flow : tg.getFlows()){
			flowsToNets.add(sortNetworkIDs(flow));
			chunksToAllocate.add(flow.getTokens());
		}

		List<Integer> flow_order = sortByFlowCriticality();
		//System.out.println("FlowOrder GreOn: "+flow_order.toString());
		for (int t=0; t<ng.getTimeslots(); t++){
			//assign each active flow to best matching network
			for(int f0=0;f0<tg.getFlows().size();f0++){
				int f=flow_order.get(f0);
				Flow flow= tg.getFlows().get(f);
				int chunksMaxTp = (int)(flow.getTokensMax()/flow.getWindowMax());	//get average maximum throughput for later allocation
				//assign only within preferred time frame of flow
				if(flow.getStartTime()<=t && flow.getDeadline()>t){
					int n0=0;					
					int allocated=0;
					
					//try to allocate at networks according to preference. 
					while(allocated==0 && n0<ng.getNetworks().size()){
						int n=flowsToNets.get(f).get(n0);
						//oppertunistic scheduling: use benefit estimation to decide whether to use network or not
						if(scheduleDecision(f, n, t)){
							if(chunksToAllocate.get(f)<chunksMaxTp){
								allocated=allocate(f, t, n, chunksToAllocate.get(f)); //do not allocate more chunks than required
							}else{
								allocated=allocate(f, t, n, chunksMaxTp);
							}
							if(allocated>0){
								chunksToAllocate.set(f, chunksToAllocate.get(f)-allocated);
								if(NEW_RATING_ESTIMATOR){
									cs.updateStatefulReward(f0, t, allocated);
								}
							}
						}
						n0++; //Try next preferred if allocation failed. 
					}
					
				}
			}
			
		}

	}


	protected boolean scheduleDecision(int f, int n, int t) {
		return true;
	}

	
	@Override
	public String getType() {
		return "GreedyOnline"+(NEW_RATING_ESTIMATOR?"_H2":"");
	}

}
